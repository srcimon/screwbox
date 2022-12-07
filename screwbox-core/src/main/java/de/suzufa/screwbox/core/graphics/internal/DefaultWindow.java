package de.suzufa.screwbox.core.graphics.internal;

import static java.util.Arrays.asList;
import static java.util.Comparator.reverseOrder;
import static java.util.Objects.nonNull;

import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
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

    @Deprecated
    private final WindowFrame frame;
    private final FrameAdapter frameAdapter;
    @Deprecated
    private final GraphicsDevice graphicsDevice;
    private final GraphicsConfiguration configuration;
    private final DefaultScreen screen;
    private DisplayMode lastDisplayMode;
    private final ExecutorService executor;
    private Cursor windowCursor = cursorFrom(MouseCursor.DEFAULT);
    private Cursor fullscreenCursor = cursorFrom(MouseCursor.HIDDEN);
    private Offset lastOffset;

    public DefaultWindow(final FrameAdapter frameAdapter, final WindowFrame frame,
            final GraphicsConfiguration configuration,
            final ExecutorService executor,
            final DefaultScreen screen) {
        this.frameAdapter = frameAdapter;
        this.graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        this.frame = frame;
        this.configuration = configuration;
        this.executor = executor;
        this.screen = screen;
        configuration.registerListener(this);
    }

    @Override
    public Offset position() {
        final var bounds = frameAdapter.bounds();
        return Offset.at(bounds.x, bounds.y);
    }

    @Override
    public Window setTitle(final String title) {
        frameAdapter.setTitle(title);
        return this;
    }

    @Override
    public Window moveTo(final Offset position) {
        if (configuration.isFullscreen()) {
            throw new IllegalStateException("Can't move Window in fullscreen.");
        }
        frame.setBounds(position.x(), position.y(), frameAdapter.width(), frameAdapter.height());
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
        frame.createBufferStrategy(2);
        frame.setBounds(0, 0, width, height);
        if (configuration.isFullscreen()) {
            lastDisplayMode = frameAdapter.displayMode();
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
            lastOffset = Offset.at(frameAdapter.bounds().x, frameAdapter.bounds().y);
        }
        return this;
    }

    public List<Dimension> supportedResolutions() {
        return asList(frameAdapter.displayModes()).stream()
                .map(this::toDimension)
                .distinct()
                .sorted(reverseOrder())
                .toList();
    }

    @Override
    public void configurationChanged(final ConfigurationProperty changedProperty) {
        final boolean mustReopenWindow = List.of(ConfigurationProperty.WINDOW_MODE, ConfigurationProperty.RESOLUTION)
                .contains(changedProperty);

        if (mustReopenWindow && frameAdapter.isVisible()) {
            close();
            open();
        }
    }

    @Override
    public boolean hasFocus() {
        return frameAdapter.hasFocus();
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
        this.frameAdapter.setCursor(configuration.isFullscreen() ? fullscreenCursor : windowCursor);

    }

    private Cursor createCustomCursor(final Image image) {
        return frameAdapter.createCustomCursor(image, new Point(0, 0), "custom cursor");
    }

    @Override
    public String title() {
        return frameAdapter.title();
    }

    public Dimension currentResolution() {
        final var screenSize = frameAdapter.displayMode();
        return toDimension(screenSize);
    }

    private Dimension toDimension(final DisplayMode screenSize) {
        return Dimension.of(screenSize.getWidth(), screenSize.getHeight());
    }

}