package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Font;
import io.github.srcimon.screwbox.core.graphics.*;
import io.github.srcimon.screwbox.core.window.internal.WindowFrame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Supplier;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
import static java.lang.Math.round;
import static java.util.Objects.nonNull;

public class DefaultScreen implements Screen {

    private Renderer renderer;
    private final WindowFrame frame;
    private final Robot robot;
    private Graphics2D lastGraphics;

    public DefaultScreen(final WindowFrame frame, final Renderer renderer, final Robot robot) {
        this.renderer = renderer;
        this.frame = frame;
        this.robot = robot;
    }

    public void updateScreen(final boolean antialiased) {
        final Supplier<Graphics2D> graphicsSupplier = () -> {
            frame.getCanvas().getBufferStrategy().show();
            if (nonNull(lastGraphics)) {
                lastGraphics.dispose();
            }
            final Graphics2D graphics = (Graphics2D) frame.getCanvas().getBufferStrategy().getDrawGraphics();

            if (antialiased) {
                graphics.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
            }
            lastGraphics = graphics;
            return graphics;
        };
        renderer.updateGraphicsContext(graphicsSupplier, frame.getCanvasSize());
    }

    public void setRenderer(final Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public Screen fillWith(final Color color) {
        renderer.fillWith(color);
        return this;
    }

    @Override
    public Screen drawRectangle(final Offset origin, final Size size, final RectangleDrawOptions options) {
        renderer.drawRectangle(origin, size, options);
        return this;
    }

    @Override
    public Screen drawLine(final Offset from, final Offset to, final LineDrawOptions options) {
        renderer.drawLine(from, to, options);
        return this;
    }

    @Override
    public Screen drawCircle(final Offset offset, final int radius, final CircleDrawOptions options) {
        renderer.drawCircle(offset, radius, options);
        return this;
    }

    @Override
    public Screen fillCircle(final Offset offset, final int diameter, final Color color) {
        renderer.fillCircle(offset, diameter, color);
        return this;
    }

    @Override
    public Screen fillWith(final Offset offset, final Sprite sprite, final double scale, final Percent opacity) {
        final long spriteWidth = round(sprite.size().width() * scale);
        final long spriteHeight = round(sprite.size().height() * scale);
        final long countX = frame.getWidth() / spriteWidth + 1;
        final long countY = frame.getHeight() / spriteHeight + 1;
        final long offsetX = offset.x() % spriteWidth - spriteWidth;
        final long offsetY = offset.y() % spriteHeight - spriteHeight;

        for (long x = 0; x <= countX + 1; x++) {
            for (long y = 0; y <= countY + 1; y++) {
                final Offset thisOffset = Offset.at((double) x * spriteWidth + offsetX, (double) y * spriteHeight + offsetY);
                drawSprite(sprite, thisOffset, scale, opacity, Rotation.none(), Flip.NONE, null);
            }
        }
        return this;
    }

    @Override
    public Sprite takeScreenshot() {
        if (!frame.isVisible()) {
            throw new IllegalStateException("window must be opend first to create screenshot");
        }
        final int menuBarHeight = frame.getJMenuBar() == null ? 0 : frame.getJMenuBar().getHeight();
        final Rectangle rectangle = new Rectangle(frame.getX(),
                frame.getY() + frame.getInsets().top + menuBarHeight,
                frame.getCanvas().getWidth(),
                frame.canvasHeight());

        final BufferedImage screenCapture = robot.createScreenCapture(rectangle);
        return Sprite.fromImage(screenCapture);
    }

    @Override
    public Screen drawText(final Offset offset, final String text, final Font font, final Color color) {
        renderer.drawText(offset, text, font, color);
        return this;
    }

    @Override
    public Screen drawTextCentered(final Offset position, final String text, final Font font, final Color color) {
        renderer.drawTextCentered(position, text, font, color);
        return this;
    }

    @Override
    public Screen drawSprite(final Sprite sprite, final Offset origin, final double scale, final Percent opacity,
                             final Rotation rotation, final Flip flip, final ScreenBounds clip) {
        renderer.drawSprite(sprite, origin, scale, opacity, rotation, flip, clip);
        return this;
    }

    @Override
    public Screen drawTextCentered(final Offset offset, final String text, final Pixelfont font,
                                   final Percent opacity, final double scale) {
        final int totalWidth = (int) (font.widthOf(text) * scale);
        drawTextSprites(offset.addX(totalWidth / -2), opacity, scale, font.spritesFor(text), font);
        return this;
    }

    @Override
    public Screen drawText(final Offset offset, final String text, final Pixelfont font, final Percent opacity,
                           final double scale) {
        final List<Sprite> allSprites = font.spritesFor(text);
        drawTextSprites(offset, opacity, scale, allSprites, font);
        return this;
    }

    @Override
    public Offset center() {
        return size().center();
    }

    @Override
    public Size size() {
        return frame.getCanvasSize();
    }

    @Override
    public boolean isVisible(final ScreenBounds bounds) {
        return screenBounds().intersects(bounds);
    }

    @Override
    public boolean isVisible(final Offset offset) {
        return screenBounds().contains(offset);
    }

    @Override
    public Screen drawFadingCircle(final Offset offset, final int diameter, final Color color) {
        renderer.drawFadingCircle(offset, diameter, color);
        return this;
    }

    @Override
    public Screen drawSprite(final Supplier<Sprite> sprite, final Offset origin, final double scale,
                             final Percent opacity, final Rotation rotation,
                             final Flip flip, final ScreenBounds clipArea) {
        renderer.drawSprite(sprite, origin, scale, opacity, rotation, flip, clipArea);
        return this;
    }

    @Override
    public Screen drawCircle(final Offset offset, final int diameter, final Color color, final int strokeWidth) {
        renderer.drawCircle(offset, diameter, color, strokeWidth);
        return this;
    }

    @Override
    public Offset position() {
        final var bounds = frame.getBounds();
        return Offset.at(bounds.x, bounds.y - frame.canvasHeight() + bounds.height);
    }

    private void drawTextSprites(final Offset offset, final Percent opacity, final double scale,
                                 final List<Sprite> allSprites,
                                 final Pixelfont font) {
        Offset currentOffset = offset;
        for (final var sprite : allSprites) {
            drawSprite(sprite, currentOffset, scale, opacity, Rotation.none(), Flip.NONE, null);
            currentOffset = currentOffset.addX((int) ((sprite.size().width() + font.padding()) * scale));
        }
    }

    private ScreenBounds screenBounds() {
        return new ScreenBounds(Offset.origin(), size());
    }

}
