package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.graphics.*;
import io.github.srcimon.screwbox.core.window.internal.WindowFrame;

import java.util.List;
import java.util.function.Supplier;

import static java.lang.Math.round;

public class DefaultScreen implements Screen {

    private Renderer renderer;
    private final WindowFrame frame;

    public DefaultScreen(WindowFrame frame, Renderer renderer) {
        this.renderer = renderer;
        this.frame = frame;
    }

    private Color drawColor = Color.WHITE;

    @Override
    public Screen drawColor(final Color color) {
        drawColor = color;
        return this;
    }

    @Override
    public Color drawColor() {
        return drawColor;
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
    public Screen fillRectangle(final ScreenBounds bounds, final Color color) {
        renderer.fillRectangle(bounds, color);
        return this;
    }

    @Override
    public Sprite takeScreenshot() {
        return renderer.takeScreenshot();
    }

    @Override
    public Screen fillWith(final Color color) {
        renderer.fillWith(color);
        return this;
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
                             final Rotation rotation, final Flip flip, final ScreenBounds clipArea) {
        renderer.drawSprite(sprite, origin, scale, opacity, rotation, flip, clipArea);
        return this;
    }

    @Override
    public Screen drawTextCentered(final Offset offset, final String text, final Pixelfont font,
                                   final Percent opacity, final double scale) {
        final List<Sprite> allSprites = font.spritesFor(text);
        int totalWith = 0;
        for (final var sprite : allSprites) {
            totalWith += (int) ((sprite.size().width() + font.padding()) * scale);
        }
        drawTextSprites(offset.addX(totalWith / -2), opacity, scale, allSprites, font);
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
    public Screen drawRectangle(final Offset offset, final Size size, final Rotation rotation, Color color) {
        renderer.drawRectangle(offset, size, rotation, color);
        return this;
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
    public Screen drawLine(final Offset from, final Offset to, final Color color) {
        renderer.drawLine(from, to, color);
        return this;
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
    public Screen drawCircle(final Offset offset, final int diameter, final Color color) {
        renderer.drawCircle(offset, diameter, color);
        return this;
    }

    public void updateScreen(final boolean antialiased) {
        renderer.updateScreen(antialiased);
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
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

    @Override
    public Offset position() {
        final var bounds = frame.getBounds();
        return Offset.at(bounds.x, bounds.y - frame.canvasHeight() + bounds.height);
    }

    private ScreenBounds screenBounds() {
        return new ScreenBounds(Offset.origin(), size());
    }
}
