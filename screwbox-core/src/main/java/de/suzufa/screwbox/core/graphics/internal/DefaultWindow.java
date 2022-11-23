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
import java.awt.Toolkit;
import java.util.List;
import java.util.concurrent.ExecutorService;

import de.suzufa.screwbox.core.graphics.Color;
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
    private Renderer renderer = new StandbyRenderer();
    private DisplayMode lastDisplayMode;
    private final ExecutorService executor;
    private Cursor windowCursor = cursorFrom(MouseCursor.DEFAULT);
    private Cursor fullscreenCursor = cursorFrom(MouseCursor.HIDDEN);
    private Offset lastOffset;

    public DefaultWindow(final WindowFrame frame, final GraphicsConfiguration configuration,
            final ExecutorService executor,
            final String title) {
        this.graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        this.frame = frame;
        this.configuration = configuration;
        this.executor = executor;
        setTitle(title);
        configuration.registerListener(this);
    }

    public void updateScreen(final boolean antialiased) {
        renderer.updateScreen(antialiased);
        renderer.fillWith(Color.BLACK);
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
        frame.createBufferStrategy(2);
        frame.setBounds(0, 0, width, height);
        if (configuration.isFullscreen()) {
            lastDisplayMode = graphicsDevice.getDisplayMode();
            final int bitDepth = graphicsDevice.getDisplayMode().getBitDepth();
            final int refreshRate = graphicsDevice.getDisplayMode().getRefreshRate();
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
        renderer = new SeparateThreadRenderer(new DefaultRenderer(frame), executor);
        updateCursor();
        return this;
    }

    @Override
    public Window close() {
        renderer = new StandbyRenderer();
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
        final boolean mustReopenWindow = List.of(ConfigurationProperty.WINDOW_MODE, ConfigurationProperty.RESOLUTION)
                .contains(changedProperty);

        if (mustReopenWindow && frame.isVisible()) {
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
        final var screenSize = graphicsDevice.getDisplayMode();
        return toDimension(screenSize);
    }

    private Dimension toDimension(final DisplayMode screenSize) {
        return Dimension.of(screenSize.getWidth(), screenSize.getHeight());
    }

}