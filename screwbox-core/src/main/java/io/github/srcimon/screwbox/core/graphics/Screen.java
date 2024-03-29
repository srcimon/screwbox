package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;

import java.util.function.Supplier;

/**
 * Access drawing operations on the game screen.
 *
 * @see World
 */
public interface Screen {

    /**
     * Returns the position of the {@link Screen} relative to the monitor.
     */
    Offset position();

    /**
     * Returns the {@link Size} of the {@link Screen}.
     */
    Size size();

    /**
     * Returns the center position of the {@link Screen}.
     */
    Offset center();

    /**
     * Fills the whole {@link Screen} with the given {@link Color}.
     */
    Screen fillWith(Color color);

    /**
     * Draws a rectangle on the screen using the given {@link ScreenBounds} and {@link RectangleDrawOptions}.
     *
     * @see #drawRectangle(ScreenBounds, RectangleDrawOptions)
     */
    Screen drawRectangle(final Offset origin, final Size size, final RectangleDrawOptions options);

    /**
     * Draws a rectangle on the {@link Screen} using the given {@link ScreenBounds} and {@link RectangleDrawOptions}.
     *
     * @see #drawRectangle(Offset, Size, RectangleDrawOptions)
     */
    default Screen drawRectangle(final ScreenBounds bounds, final RectangleDrawOptions options) {
        return drawRectangle(bounds.offset(), bounds.size(), options);
    }

    /**
     * Takes a sceenshot of the whole {@link Screen}. This operation is very slow and will propably cause a small lag.
     * The screenshot may also include other applications that are in front of your game screen.
     */
    Sprite takeScreenshot();

    /**
     * Draws a line on the {@link Screen} using the given {@link Offset}s and {@link LineDrawOptions}.
     */
    Screen drawLine(Offset from, Offset to, LineDrawOptions options);

    /**
     * Draws a circle on the {@link Screen} using the given position and {@link CircleDrawOptions}.
     */
    Screen drawCircle(Offset offset, int radius, CircleDrawOptions options);

    /**
     * Draws a {@link Sprite} on the {@link Screen} using the given origin and {@link SpriteDrawOptions}.
     *
     * @see #drawSprite(Sprite, Offset, SpriteDrawOptions)
     * @see #drawSprite(Sprite, Offset, SpriteDrawOptions, ScreenBounds)
     */
    Screen drawSprite(Supplier<Sprite> sprite, Offset origin, SpriteDrawOptions options);

    /**
     * Draws a {@link Sprite} on the {@link Screen} using the given origin and {@link SpriteDrawOptions}.
     *
     * @see #drawSprite(Supplier, Offset, SpriteDrawOptions)
     * @see #drawSprite(Sprite, Offset, SpriteDrawOptions, ScreenBounds)
     */
    Screen drawSprite(Sprite sprite, Offset origin, SpriteDrawOptions options);

    /**
     * Draws a {@link Sprite} on the {@link Screen} using the given origin and {@link SpriteDrawOptions}.
     *
     * @see #drawSprite(Supplier, Offset, SpriteDrawOptions)
     * @see #drawSprite(Sprite, Offset, SpriteDrawOptions)
     */
    Screen drawSprite(Sprite sprite, Offset origin, SpriteDrawOptions options, ScreenBounds clip);

    /**
     * Draws text on the {@link Screen} using {@link TextDrawOptions}. Be warned: The used fonts are system specific and
     * drawing text is kind of slow.
     */
    Screen drawText(Offset offset, String text, TextDrawOptions options);

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

    default Screen drawTextCentered(Offset offset, String text) {
        return drawTextCentered(offset, text, Pixelfont.defaultFont(), Percent.max());
    }

    default Screen drawTextCentered(Offset offset, String text, Pixelfont font, double scale) {
        return drawTextCentered(offset, text, font, Percent.max(), scale);
    }

    default Screen drawTextCentered(Offset offset, String text, double scale) {
        return drawTextCentered(offset, text, Pixelfont.defaultFont(), Percent.max(), scale);
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

    /**
     * Returns {@code true} if the given {@link ScreenBounds} is within the{@link Screen} area.
     */
    boolean isVisible(ScreenBounds bounds);

    /**
     * Returns {@code true} if the given {@link Offset} is within the {@link Screen} area.
     */
    boolean isVisible(Offset offset);

}
