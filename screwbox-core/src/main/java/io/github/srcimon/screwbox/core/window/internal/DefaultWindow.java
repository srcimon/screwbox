package io.github.srcimon.screwbox.core.window.internal;

import io.github.srcimon.screwbox.core.graphics.GraphicsConfiguration;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.internal.DefaultScreen;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;
import io.github.srcimon.screwbox.core.graphics.internal.renderer.StandbyRenderer;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;
import io.github.srcimon.screwbox.core.utils.Latch;
import io.github.srcimon.screwbox.core.window.FilesDropedOnWindow;
import io.github.srcimon.screwbox.core.window.MouseCursor;
import io.github.srcimon.screwbox.core.window.Window;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static io.github.srcimon.screwbox.core.graphics.GraphicsConfigurationEvent.ConfigurationProperty.RESOLUTION;
import static io.github.srcimon.screwbox.core.graphics.GraphicsConfigurationEvent.ConfigurationProperty.WINDOW_MODE;
import static java.util.Objects.nonNull;

public class DefaultWindow implements Window, Updatable {

    private final WindowFrame frame;
    private final GraphicsDevice graphicsDevice;
    private final GraphicsConfiguration configuration;
    private final DefaultScreen screen;
    private final Renderer renderer;
    private final Latch<FilesDropedOnWindow> filesDroppedOnWindow = Latch.of(null, null);

    private DisplayMode lastDisplayMode;
    private Supplier<Cursor> windowCursor = cursorFrom(MouseCursor.DEFAULT);
    private Supplier<Cursor> fullscreenCursor = cursorFrom(MouseCursor.HIDDEN);
    private Offset lastOffset;

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
        new DragAndDropSupport(frame, (files, position) -> filesDroppedOnWindow.assignActive(new FilesDropedOnWindow(files, position)));
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
        updateCursor();
        return this;
    }

    @Override
    public Window moveTo(final Offset position) {
        if (configuration.isFullscreen()) {
            throw new IllegalStateException("cannot move Window in fullscreen");
        }
        frame.setBounds(position.x(), position.y(), frame.getWidth(), frame.getHeight());
        return this;
    }

    @Override
    public Optional<FilesDropedOnWindow> filesDropedOnWindow() {
        return Optional.ofNullable(filesDroppedOnWindow.inactive());
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
    public Window setFullscreenCursor(final Sprite cursor) {
        fullscreenCursor = createCustomCursor(cursor);
        updateCursor();
        return this;
    }

    @Override
    public Window setWindowCursor(final Sprite cursor) {
        windowCursor = createCustomCursor(cursor);
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
    public Window setCursor(final Sprite cursor) {
        final var customCursor = createCustomCursor(cursor);
        windowCursor = customCursor;
        fullscreenCursor = customCursor;
        updateCursor();
        return this;
    }

    @Override
    public Window setCursor(final MouseCursor cursor) {
        final var customCursor = cursorFrom(cursor);
        windowCursor = customCursor;
        fullscreenCursor = customCursor;
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
        filesDroppedOnWindow.toggle();
        filesDroppedOnWindow.assignActive(null);
    }

    private Supplier<Cursor> cursorFrom(final MouseCursor cursor) {
        if (MouseCursor.DEFAULT == cursor) {
            return Cursor::getDefaultCursor;
        }
        if (MouseCursor.HIDDEN == cursor) {
            return createCustomCursor(Sprite.invisible());
        }
        throw new IllegalStateException("Unknown mouse cursor: " + cursor);
    }

    private void updateCursor() {
        final var cursor = configuration.isFullscreen() ? fullscreenCursor : windowCursor;
        this.frame.setCursor(cursor.get());

    }

    private Supplier<Cursor> createCustomCursor(final Sprite sprite) {
        return () -> Toolkit.getDefaultToolkit().createCustomCursor(sprite.singleImage(), new Point(0, 0),
                "custom cursor");
    }
}