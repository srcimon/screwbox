package de.suzufa.screwbox.core.graphics.internal;

import static de.suzufa.screwbox.core.graphics.Offset.origin;
import static java.lang.Math.round;

import java.util.List;

import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Flip;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Pixelfont;
import de.suzufa.screwbox.core.graphics.Screen;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.Window;
import de.suzufa.screwbox.core.graphics.WindowBounds;

public class DefaultScreen implements Screen {

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
        final double offsetX = offset.x() % spriteWidth;
        final double offsetY = offset.y() % spriteHeight;

        for (long x = 0; x <= countX; x++) {
            for (long y = 0; y <= countY; y++) {
                final Offset thisOffset = Offset.at(x * spriteWidth + offsetX, y * spriteHeight + offsetY);
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
    public Window drawTextCentered(final Offset offset, final String text, final Pixelfont font,
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
    public Window drawText(final Offset offset, final String text, final Pixelfont font, final Percent opacity,
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
        final var bounds = frame.getBounds();
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
    public Window drawFadingCircle(final Offset offset, final int diameter, final Color color) {
        renderer.drawFadingCircle(offset, diameter, color);
        return this;
    }

    @Override
    public Window drawSprite(final Asset<Sprite> sprite, final Offset origin, final double scale,
            final Percent opacity, final Angle rotation,
            final Flip flip, final WindowBounds clipArea) {
        renderer.drawSprite(sprite, origin, scale, opacity, rotation, flip, clipArea);
        return this;
    }

    @Override
    public Window drawCircle(final Offset offset, final int diameter, final Color color) {
        renderer.drawCircle(offset, diameter, color);
        return this;
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
}
