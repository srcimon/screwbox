package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;

import java.util.function.Supplier;

public interface Screen {

    /**
     * Returns the position of the {@link Screen} relative to the monitor.
     */
    Offset position();

    Screen drawColor(Color color);

    Color drawColor();

    Screen fillRectangle(ScreenBounds bounds, Color color);

    Screen fillWith(Color color);

    Sprite takeScreenshot();

    default Screen fill() {
        return fillWith(drawColor());
    }

    default Screen fillRectangle(final ScreenBounds bounds) {
        return fillRectangle(bounds, drawColor());
    }

    default Screen fillRectangle(final Offset origin, final Size size, final Color color) {
        return fillRectangle(new ScreenBounds(origin, size), color);
    }

    default Screen fillRectangle(final Offset origin, final Size size) {
        return fillRectangle(new ScreenBounds(origin, size), drawColor());
    }

    Screen drawFadingCircle(Offset offset, int diameter, Color color);

    Screen drawCircle(Offset offset, int diameter, Color color, int strokeWidth);

    default Screen drawCircle(Offset offset, int diameter, Color color) {
        return drawCircle(offset, diameter, color, 1);
    }

    Screen fillCircle(Offset offset, int diameter, Color color);

    default Screen fillCircle(final Offset offset, final int diameter) {
        return fillCircle(offset, diameter, drawColor());
    }

    Screen drawSprite(Supplier<Sprite> sprite, Offset origin, double scale, Percent opacity, Rotation rotation,
                      Flip flip, ScreenBounds clipArea);

    default Screen drawSprite(Supplier<Sprite> sprite, Offset origin, double scale, Percent opacity) {
        return drawSprite(sprite, origin, scale, opacity, Rotation.none(), Flip.NONE, null);
    }

    default Screen drawSprite(Supplier<Sprite> sprite, Offset origin, double scale, Percent opacity, Rotation rotation) {
        return drawSprite(sprite, origin, scale, opacity, rotation, Flip.NONE, null);
    }

    default Screen drawSprite(final Supplier<Sprite> sprite, final Offset origin, final Percent opacity) {
        return drawSprite(sprite, origin, 1, opacity, Rotation.none(), Flip.NONE, null);
    }

    default Screen drawSprite(final Supplier<Sprite> sprite, final Offset origin) {
        return drawSprite(sprite, origin, Percent.max());
    }

    Screen drawSprite(Sprite sprite, Offset origin, double scale, Percent opacity, Rotation rotation,
                      Flip flip, ScreenBounds clipArea);

    default Screen drawSprite(Sprite sprite, Offset origin, double scale, Percent opacity) {
        return drawSprite(sprite, origin, scale, opacity, Rotation.none(), Flip.NONE, null);
    }

    default Screen drawSprite(Sprite sprite, Offset origin, double scale, Percent opacity, Rotation rotation) {
        return drawSprite(sprite, origin, scale, opacity, rotation, Flip.NONE, null);
    }

    default Screen drawSprite(final Sprite sprite, final Offset origin, final Percent opacity) {
        return drawSprite(sprite, origin, 1, opacity, Rotation.none(), Flip.NONE, null);
    }

    default Screen drawSprite(final Sprite sprite, final Offset origin) {
        return drawSprite(sprite, origin, Percent.max());
    }

    Screen drawText(Offset offset, String text, Pixelfont font, Percent opacity, double scale);

    default Screen drawText(Offset offset, String text, Pixelfont font, Percent opacity) {
        return drawText(offset, text, font, opacity, 1);
    }

    default Screen drawText(Offset offset, String text, Pixelfont font) {
        return drawText(offset, text, font, Percent.max());
    }

    default Screen drawText(Offset offset, String text, Pixelfont font, double scale) {
        return drawText(offset, text, font, Percent.max(), scale);
    }

    Screen drawTextCentered(Offset offset, String text, Pixelfont font, Percent opacity, double scale);

    default Screen drawTextCentered(Offset offset, String text, Pixelfont font, Percent opacity) {
        return drawTextCentered(offset, text, font, opacity, 1);
    }

    default Screen drawTextCentered(Offset offset, String text, Pixelfont font) {
        return drawTextCentered(offset, text, font, Percent.max());
    }

    default Screen drawTextCentered(Offset offset, String text, Pixelfont font, double scale) {
        return drawTextCentered(offset, text, font, Percent.max(), scale);
    }

    Screen drawText(Offset offset, String text, Font font, Color color);

    default Screen drawText(final Offset offset, final String text, final Font font) {
        return drawText(offset, text, font, drawColor());
    }

    Screen drawTextCentered(final Offset position, final String text, final Font font, final Color color);

    default Screen drawTextCentered(final Offset offset, final String text, final Font font) {
        return drawTextCentered(offset, text, font, drawColor());
    }

    Screen fillWith(Offset offset, Sprite sprite, double scale, Percent opacity);

    default Screen fillWith(final Sprite sprite, final double scale) {
        return fillWith(Offset.origin(), sprite, scale, Percent.max());
    }

    default Screen fillWith(final Offset offset, final Sprite sprite, final double scale) {
        return fillWith(offset, sprite, scale, Percent.max());
    }

    default Screen fillWith(final Offset offset, final Sprite sprite) {
        return fillWith(offset, sprite, 1);
    }

    default Screen fillWith(final Sprite sprite) {
        return fillWith(sprite, 1);
    }

    Screen drawLine(Offset from, Offset to, Color color);

    default Screen drawLine(final Offset from, final Offset to) {
        return drawLine(from, to, drawColor());
    }

    /**
     * Returns {@code true} if the given {@link ScreenBounds} is within the
     * {@link Screen} area.
     */
    boolean isVisible(ScreenBounds bounds);

    /**
     * Returns {@code true} if the given {@link Offset} is within the {@link Screen}
     * area.
     */
    boolean isVisible(Offset offset);

    Size size();

    Offset center();

    Screen drawRectangle(final Offset offset, final Size size, final Rotation rotation, final Color color);

    default Screen drawRectangle(final Offset offset, final Size size, final Color color) {
        return drawRectangle(offset, size, Rotation.none(), color);
    }
}
