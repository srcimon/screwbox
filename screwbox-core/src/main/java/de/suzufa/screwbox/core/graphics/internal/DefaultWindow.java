package de.suzufa.screwbox.core.graphics.internal;

import static de.suzufa.screwbox.core.graphics.GraphicsConfigurationListener.ConfigurationProperty.RESOLUTION;
import static de.suzufa.screwbox.core.graphics.GraphicsConfigurationListener.ConfigurationProperty.WINDOW_MODE;
import static java.util.Arrays.asList;
import static java.util.Comparator.reverseOrder;
import static java.util.Objects.nonNull;

import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.List;
import java.util.concurrent.ExecutorService;

import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Frame;
import de.suzufa.screwbox.core.graphics.GraphicsConfiguration;
import de.suzufa.screwbox.core.graphics.GraphicsConfigurationListener;
import de.suzufa.screwbox.core.graphics.MouseCursor;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Window;

public class DefaultWindow implements Window, GraphicsConfigurationListener {

    private final WindowFrame frame;
    private final GraphicsDevice graphicsDevice;
    private final GraphicsConfiguration configuration;
    private final DefaultScreen screen;
    private DisplayMode lastDisplayMode;
    private final ExecutorService executor;
    private Cursor windowCursor = cursorFrom(MouseCursor.DEFAULT);
    private Cursor fullscreenCursor = cursorFrom(MouseCursor.HIDDEN);
    private Offset lastOffset;

    public DefaultWindow(final WindowFrame frame,
            final GraphicsConfiguration configuration,
            final ExecutorService executor,
            final DefaultScreen screen) {
        this.graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        this.frame = frame;
        this.configuration = configuration;
        this.executor = executor;
        this.screen = screen;
        configuration.registerListener(this);
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
    public Window open() {
        final int width = configuration.resolution().width();
        final int height = configuration.resolution().height();

        frame.dispose();
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setVisible(true);
        if (configuration.isFullscreen()) {
            if (nonNull(graphicsDevice.getFullScreenWindow())) {
                throw new IllegalStateException("can not replace current fullscreen window");
            }
            lastDisplayMode = graphicsDevice.getDisplayMode();
            final int bitDepth = lastDisplayMode.getBitDepth();
            final int refreshRate = lastDisplayMode.getRefreshRate();
            final DisplayMode displayMode = new DisplayMode(width, height, bitDepth, refreshRate);
            graphicsDevice.setDisplayMode(displayMode);
            graphicsDevice.setFullScreenWindow(frame);
        } else {
            if (nonNull(lastOffset)) {
                moveTo(lastOffset);
            } else {
                frame.setLocationRelativeTo(null);
            }
        }
        executor.submit(new InitializeFontDrawingTask());

        screen.setRenderer(new SeparateThreadRenderer(new DefaultRenderer(frame), executor));
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
            lastOffset = Offset.at(frame.getBounds().x, frame.getBounds().y);
        }
        return this;
    }

    public List<Dimension> supportedResolutions() {
        return asList(graphicsDevice.getDisplayModes()).stream()
                .map(this::toDimension)
                .distinct()
                .sorted(reverseOrder())
                .toList();
    }

    @Override
    public void configurationChanged(final ConfigurationProperty changedProperty) {
        if (List.of(WINDOW_MODE, RESOLUTION).contains(changedProperty) && frame.isVisible()) {
            close();
            open();
        }
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
    public Window setWindowCursor(final MouseCursor cursor) {
        windowCursor = cursorFrom(cursor);
        return this;
    }

    @Override
    public Window setFullscreenCursor(final MouseCursor cursor) {
        fullscreenCursor = cursorFrom(cursor);
        return this;
    }

    private Cursor cursorFrom(final MouseCursor cursor) {
        if (MouseCursor.DEFAULT == cursor) {
            return Cursor.getDefaultCursor();
        }
        if (MouseCursor.HIDDEN == cursor) {
            return createCustomCursor(Frame.invisible().image());
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

    @Override
    public String title() {
        return frame.getTitle();
    }

    public Dimension currentResolution() {
        return toDimension(graphicsDevice.getDisplayMode());
    }

    private Dimension toDimension(final DisplayMode screenSize) {
        return Dimension.of(screenSize.getWidth(), screenSize.getHeight());
    }

}