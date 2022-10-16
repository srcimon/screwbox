package de.suzufa.screwbox.core.graphics.internal;

import static java.lang.Math.round;
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

import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.FlipMode;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Frame;
import de.suzufa.screwbox.core.graphics.GraphicsConfiguration;
import de.suzufa.screwbox.core.graphics.GraphicsConfigurationListener;
import de.suzufa.screwbox.core.graphics.Lightmap;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Pixelfont;
import de.suzufa.screwbox.core.graphics.PredefinedCursor;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.WindowBounds;

public class DefaultWindow implements Window, GraphicsConfigurationListener {

    private final WindowFrame frame;
    private final GraphicsDevice graphicsDevice;
    private final GraphicsConfiguration configuration;
    private Renderer renderer = new StandbyRenderer();
    private DisplayMode lastDisplayMode;
    private Color drawColor = Color.WHITE;
    private final ExecutorService executor;
    private Cursor windowCursor = cursorFrom(PredefinedCursor.DEFAULT);
    private Cursor fullscreenCursor = cursorFrom(PredefinedCursor.HIDDEN);
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

    @Override
    public Window drawTextCentered(final Offset offset, final String text, final Pixelfont font,
            final Percentage opacity, final double scale) {
        final List<Sprite> allSprites = font.spritesFor(text);
        int totalWith = 0;
        for (final var sprite : allSprites) {
            totalWith += (int) ((sprite.size().width() + font.padding()) * scale);
        }
        drawTextSprites(offset.addX(totalWith / -2), opacity, scale, allSprites, font);
        return this;
    }

    @Override
    public Window drawText(final Offset offset, final String text, final Pixelfont font, final Percentage opacity,
            final double scale) {
        final List<Sprite> allSprites = font.spritesFor(text);
        drawTextSprites(offset, opacity, scale, allSprites, font);
        return this;
    }

    private void drawTextSprites(final Offset offset, final Percentage opacity, final double scale,
            final List<Sprite> allSprites,
            final Pixelfont font) {
        Offset currentOffset = offset;
        for (final var sprite : allSprites) {
            drawSprite(sprite, currentOffset, scale, opacity, Angle.none(), FlipMode.NONE, null);
            currentOffset = currentOffset.addX((int) ((sprite.size().width() + font.padding()) * scale));
        }
    }

    @Override
    public Window drawColor(final Color color) {
        drawColor = color;
        return this;
    }

    @Override
    public Color drawColor() {
        return drawColor;
    }

    @Override
    public Window drawCircle(final Offset offset, final int diameter, final Color color) {
        renderer.drawCircle(offset, diameter, color);
        return this;
    }

    @Override
    public Window fillWith(final Offset offset, final Sprite sprite, final double scale, final Percentage opacity) {
        final long spriteWidth = round(sprite.size().width() * scale);
        final long spriteHeight = round(sprite.size().height() * scale);
        final long countX = frame.getWidth() / spriteWidth + 1;
        final long countY = frame.getHeight() / spriteHeight + 1;
        final double offsetX = offset.x() % spriteWidth;
        final double offsetY = offset.y() % spriteHeight;

        for (long x = 0; x <= countX; x++) {
            for (long y = 0; y <= countY; y++) {
                final Offset thisOffset = Offset.at(x * spriteWidth + offsetX, y * spriteHeight + offsetY);
                drawSprite(sprite, thisOffset, scale, opacity, Angle.none(), FlipMode.NONE, null);
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
    public Window drawText(final Offset offset, final String text, final Font font, final Color color) {
        renderer.drawText(offset, text, font, color);
        return this;
    }

    @Override
    public Window drawTextCentered(final Offset position, final String text, final Font font, final Color color) {
        renderer.drawTextCentered(position, text, font, color);
        return this;
    }

    @Override
    public Window drawSprite(final Sprite sprite, final Offset origin, final double scale, final Percentage opacity,
            final Angle rotation, final FlipMode flipMode, final WindowBounds clipArea) {
        renderer.drawSprite(sprite, origin, scale, opacity, rotation, flipMode, clipArea);
        return this;
    }

    @Override
    public Window drawLightmap(Lightmap lightmap) {
        return drawSprite(lightmap.createImage(), Offset.origin(), lightmap.resolution(), Percentage.of(0.75),
                Angle.none());
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
    public void configurationChanged() {
        if (frame.isVisible()) {
            close();
            open();
        }
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
    public Window setWindowCursor(final PredefinedCursor cursor) {
        windowCursor = cursorFrom(cursor);
        return this;
    }

    @Override
    public Window setFullscreenCursor(final PredefinedCursor cursor) {
        fullscreenCursor = cursorFrom(cursor);
        return this;
    }

    private Cursor cursorFrom(final PredefinedCursor cursor) {
        if (PredefinedCursor.DEFAULT == cursor) {
            return Cursor.getDefaultCursor();
        }
        if (PredefinedCursor.HIDDEN == cursor) {
            return createCustomCursor(Frame.invisible().image());
        }
        throw new IllegalStateException("Unknown predefined cursor: " + cursor);
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