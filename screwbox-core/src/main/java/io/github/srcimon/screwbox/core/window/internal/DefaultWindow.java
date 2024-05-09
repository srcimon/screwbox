package io.github.srcimon.screwbox.core.window.internal;

import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.MouseCursor;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.internal.DefaultScreen;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;
import io.github.srcimon.screwbox.core.graphics.internal.renderer.StandbyRenderer;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.window.FilesDropedOnWindow;
import io.github.srcimon.screwbox.core.window.Window;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

import static io.github.srcimon.screwbox.core.graphics.GraphicsConfigurationEvent.ConfigurationProperty.RESOLUTION;
import static io.github.srcimon.screwbox.core.graphics.GraphicsConfigurationEvent.ConfigurationProperty.WINDOW_MODE;
import static java.util.Objects.nonNull;

public class DefaultWindow implements Window, Updatable {

    private final WindowFrame frame;
    private final GraphicsDevice graphicsDevice;
    private final GraphicsConfiguration configuration;
    private final DefaultScreen screen;
    private final Renderer renderer;
    private DisplayMode lastDisplayMode;
    private Cursor windowCursor = cursorFrom(MouseCursor.DEFAULT);
    private Cursor fullscreenCursor = cursorFrom(MouseCursor.HIDDEN);
    private Offset lastOffset;
    private FilesDropedOnWindow filesDropedOnWindow;

    public DefaultWindow(final WindowFrame frame,
                         final GraphicsConfiguration configuration,
                         final DefaultScreen screen,
                         final GraphicsDevice graphicsDevice,
                         final Renderer renderer) {
        this.graphicsDevice = graphicsDevice;
        this.frame = frame;
        this.configuration = configuration;
        this.screen = screen;
        this.renderer = renderer;
        new DragAndDropSupport(frame, (files, position) -> filesDropedOnWindow = new FilesDropedOnWindow(files, position));
        configuration.addListener(event -> {
            final boolean mustReopen = List.of(WINDOW_MODE, RESOLUTION).contains(event.changedProperty());
            if (mustReopen && frame.isVisible()) {
                close();
                open();
            }
        });
    }

    @Override
    public Offset position() {
        final var bounds = frame.getBounds();
        return Offset.at(bounds.x, bounds.y);
    }

    @Override
    public Window setTitle(final String title) {
        frame.setTitle(title);
        return this;
    }

    @Override
    public Window moveTo(final Offset position) {
        if (configuration.isFullscreen()) {
            throw new IllegalStateException("Can't move Window in fullscreen.");
        }
        frame.setBounds(position.x(), position.y(), frame.getWidth(), frame.getHeight());
        return this;
    }

    @Override
    public Optional<FilesDropedOnWindow> filesDropedOnWindow() {
        return Optional.ofNullable(filesDropedOnWindow);
    }

    @Override
    public Window open() {
        if (isOpen()) {
            return this;
        }
        final int width = configuration.resolution().width();
        final int height = configuration.resolution().height();

        frame.dispose();
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setIgnoreRepaint(true);
        if (configuration.isFullscreen()) {
            if (nonNull(graphicsDevice.getFullScreenWindow())) {
                throw new IllegalStateException("can not replace current fullscreen window");
            }
            lastDisplayMode = graphicsDevice.getDisplayMode();
            final int bitDepth = lastDisplayMode.getBitDepth();
            final int refreshRate = lastDisplayMode.getRefreshRate();
            final DisplayMode displayMode = new DisplayMode(width, height, bitDepth, refreshRate);
            graphicsDevice.setDisplayMode(displayMode);
            frame.makeFullscreen(graphicsDevice);
        } else {
            if (nonNull(lastOffset)) {
                moveTo(lastOffset);
            } else {
                frame.setLocationRelativeTo(null);
            }
        }
        frame.getCanvas().createBufferStrategy(2);
        screen.setRenderer(renderer);
        updateCursor();
        return this;
    }

    @Override
    public Window close() {
        if (isClosed()) {
            return this;
        }
        screen.setRenderer(new StandbyRenderer());
        frame.setCursor(Cursor.getDefaultCursor());
        frame.dispose();

        if (nonNull(lastDisplayMode)) {
            graphicsDevice.setFullScreenWindow(null);
            graphicsDevice.setDisplayMode(lastDisplayMode);
            lastDisplayMode = null;
        } else {
            final Rectangle bounds = frame.getBounds();
            lastOffset = Offset.at(bounds.x, bounds.y);
        }
        return this;
    }

    @Override
    public boolean hasFocus() {
        return frame.hasFocus();
    }

    @Override
    public Window setFullscreenCursor(final Frame cursor) {
        fullscreenCursor = createCustomCursor(cursor.image());
        updateCursor();
        return this;
    }

    @Override
    public Window setWindowCursor(final Frame cursor) {
        windowCursor = createCustomCursor(cursor.image());
        updateCursor();
        return this;
    }

    @Override
    public Window setApplicationIcon(final Sprite icon) {
        frame.setIcon(icon);
        return this;
    }

    @Override
    public Window setWindowCursor(final MouseCursor cursor) {
        windowCursor = cursorFrom(cursor);
        return this;
    }

    @Override
    public Window setFullscreenCursor(final MouseCursor cursor) {
        fullscreenCursor = cursorFrom(cursor);
        return this;
    }

    @Override
    public Window setCursor(final Frame cursor) {
        final Image image = cursor.image();
        windowCursor = createCustomCursor(image);
        fullscreenCursor = createCustomCursor(image);
        updateCursor();
        return this;
    }

    @Override
    public Window setCursor(final MouseCursor cursor) {
        final var awtCursor = cursorFrom(cursor);
        windowCursor = awtCursor;
        fullscreenCursor = awtCursor;
        updateCursor();
        return this;
    }

    @Override
    public String title() {
        return frame.getTitle();
    }

    @Override
    public Size size() {
        return configuration.resolution();
    }

    @Override
    public boolean isOpen() {
        return frame.isVisible();
    }

    @Override
    public void update() {
        filesDropedOnWindow = null;
    }

    private Cursor cursorFrom(final MouseCursor cursor) {
        if (MouseCursor.DEFAULT == cursor) {
            return Cursor.getDefaultCursor();
        }
        if (MouseCursor.HIDDEN == cursor) {
            return createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB));
        }
        throw new IllegalStateException("Unknown mouse cursor: " + cursor);
    }

    private void updateCursor() {
        this.frame.setCursor(configuration.isFullscreen() ? fullscreenCursor : windowCursor);

    }

    private Cursor createCustomCursor(final Image image) {
        return Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0),
                "custom cursor");
    }

}