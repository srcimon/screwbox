package io.github.simonbas.screwbox.core.graphics.internal;

import io.github.simonbas.screwbox.core.Angle;
import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.assets.Asset;
import io.github.simonbas.screwbox.core.graphics.*;

import java.util.List;

import static io.github.simonbas.screwbox.core.graphics.Offset.origin;
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

        for (long x = 0; x <= countX; x++) {
            for (long y = 0; y <= countY; y++) {
                final Offset thisOffset = Offset.at((double) x * spriteWidth + offsetX, (double) y * spriteHeight + offsetY);
                drawSprite(sprite, thisOffset, scale, opacity, Angle.none(), Flip.NONE, null);
            }
        }
        return this;
    }

    @Override
    public Screen fillRectangle(final WindowBounds bounds, final Color color) {
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
            final Angle rotation, final Flip flip, final WindowBounds clipArea) {
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
        return Offset.at(size().width() / 2.0, size().height() / 2.0);
    }

    @Override
    public Dimension size() {
        final var bounds = frame.getCanvas().getBounds();
        return Dimension.of(bounds.width, bounds.height);
    }

    @Override
    public boolean isVisible(final WindowBounds bounds) {
        return bounds.intersects(windowBounds());
    }

    // TODO: cache
    private WindowBounds windowBounds() {
        return new WindowBounds(origin(), size());
    }

    // TODO: Test
    @Override
    public boolean isVisible(final Offset offset) {
        return windowBounds().contains(offset);
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
    public Screen drawSprite(final Asset<Sprite> sprite, final Offset origin, final double scale,
            final Percent opacity, final Angle rotation,
            final Flip flip, final WindowBounds clipArea) {
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
            drawSprite(sprite, currentOffset, scale, opacity, Angle.none(), Flip.NONE, null);
            currentOffset = currentOffset.addX((int) ((sprite.size().width() + font.padding()) * scale));
        }
    }

    @Override
    public Offset position() {
        final var bounds = frame.getBounds();
        return Offset.at(bounds.x, bounds.y - frame.canvasHeight() + bounds.height);
    }

}
