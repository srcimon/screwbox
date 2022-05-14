package de.suzufa.screwbox.core.graphics.internal;

import static java.lang.Math.round;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;

import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.List;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.GraphicsConfigListener;
import de.suzufa.screwbox.core.graphics.GraphicsConfiguration;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.WindowBounds;
import de.suzufa.screwbox.core.loop.Metrics;

public class DefaultWindow implements Window, GraphicsConfigListener {

    private final Frame frame;
    private final GraphicsDevice graphicsDevice;
    private final GraphicsConfiguration configuration;
    private Renderer renderer = new StandbyRenderer();
    private DisplayMode lastDisplayMode;
    private final Metrics metrics;
    private Color drawingColor = Color.WHITE;

    public DefaultWindow(final Frame frame, final GraphicsConfiguration configuration, final Metrics metrics) {
        this.graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        this.frame = frame;
        this.configuration = configuration;
        this.metrics = metrics;
        setTitle("ScrewBox");
        configuration.registerListener(this);
    }

    @Override
    public Window setDrawingColor(final Color color) {
        drawingColor = color;
        return this;
    }

    @Override
    public Color drawingColor() {
        return drawingColor;
    }

    @Override
    public Window drawCircle(final Offset offset, final int diameter, final Color color) {
        renderer.drawCircle(offset, diameter, color);
        return this;
    }

    @Override
    public Window fillWith(final Offset offset, final Sprite sprite, final double scale, final Percentage opacity) {
        final long spriteWidth = round(sprite.dimension().width() * scale);
        final long spriteHeight = round(sprite.dimension().height() * scale);
        final long countX = frame.getWidth() / spriteWidth + 1;
        final long countY = frame.getHeight() / spriteHeight + 1;
        final double offsetX = offset.x() % spriteWidth;
        final double offsetY = offset.y() % spriteHeight;

        for (long x = 0; x <= countX; x++) {
            for (long y = 0; y <= countY; y++) {
                final Offset thisOffset = Offset.at(x * spriteWidth + offsetX, y * spriteHeight + offsetY);
                drawSprite(sprite, thisOffset, scale, opacity, Rotation.none());
            }
        }
        return this;
    }

    @Override
    public Window drawRectangle(final WindowBounds bounds, final Color color) {
        renderer.drawRectangle(bounds, color);
        return this;
    }

    @Override
    public Sprite takeScreenshot() {
        return renderer.takeScreenshot();
    }

    public void updateScreen(final boolean antialiased) {
        renderer.updateScreen(antialiased);
        renderer.fillWith(Color.BLACK);
    }

    @Override
    public Offset center() {
        return Offset.at(size().width() / 2.0, size().height() / 2.0);
    }

    @Override
    public Dimension size() {
        final var bounds = frame.getBounds();
        return Dimension.of(bounds.width, bounds.height);
    }

    @Override
    public Window fillWith(final Color color) {
        renderer.fillWith(color);
        return this;
    }

    @Override
    public Window drawPolygon(final List<Offset> points, final Color color) {
        renderer.drawPolygon(points, color);
        return this;
    }

    @Override
    public Window drawText(final Offset offset, final String text, final Font font, final Color color) {
        renderer.drawText(offset, text, font, color);
        return this;
    }

    @Override
    public Window drawTextCentered(Offset position, String text, Font font, Color color) {
        renderer.drawTextCentered(position, text, font, color);
        return this;
    }

    @Override
    public Window drawSprite(final Sprite sprite, final Offset origin, final double scale, final Percentage opacity,
            final Rotation rotation) {
        renderer.drawSprite(sprite, origin, scale, opacity, rotation);
        return this;
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
        if (!frame.isResizable()) {
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
        frame.setResizable(!configuration.isFullscreen());
        frame.setVisible(true);
        frame.createBufferStrategy(2);
        frame.setBounds(0, 0, width, height);
        if (configuration.isFullscreen()) {
            makeCursorInvisible();
            lastDisplayMode = graphicsDevice.getDisplayMode();
            final int bitDepth = graphicsDevice.getDisplayMode().getBitDepth();
            final int refreshRate = graphicsDevice.getDisplayMode().getRefreshRate();
            final DisplayMode displayMode = new DisplayMode(width, height, bitDepth, refreshRate);
            graphicsDevice.setDisplayMode(displayMode);
            graphicsDevice.setFullScreenWindow(frame);
        }
        renderer = new SeparateThreadRenderer(new DefaultRenderer(frame, metrics));
        return this;
    }

    @Override
    public Window close() {
        renderer.terminate();
        renderer = new StandbyRenderer();
        frame.setCursor(Cursor.getDefaultCursor());
        frame.dispose();
        if (nonNull(lastDisplayMode)) {
            graphicsDevice.setFullScreenWindow(null);
            graphicsDevice.setDisplayMode(lastDisplayMode);
            lastDisplayMode = null;
        }
        return this;
    }

    public List<Dimension> supportedResolutions() {
        return asList(graphicsDevice.getDisplayModes()).stream().map(dm -> Dimension.of(dm.getWidth(), dm.getHeight()))
                .toList();
    }

    @Override
    public void configurationChanged() {
        if (frame.isVisible()) {
            close();
            open();
        }
    }

    private void makeCursorInvisible() {
        final Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(Sprite.invisible().getFirstImage(),
                new Point(0, 0), "blank cursor");
        frame.setCursor(blankCursor);
    }

    @Override
    public boolean isVisible(final WindowBounds bounds) {
        return bounds.intersects(new WindowBounds(Offset.origin(), size()));
    }

    @Override
    public Window drawLine(final Offset from, final Offset to, final Color color) {
        renderer.drawLine(from, to, color);
        return this;
    }

}
