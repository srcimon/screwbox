package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;

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
     * Fills the {@link ScreenBounds} with the given {@link Color}.
     * 
     * @see #fillRectangle(Offset, Size, Color) 
     */
    Screen fillRectangle(ScreenBounds bounds, Color color);

    /**
     * Fills the area markt by origin and size with the given {@link Color}.
     *
     * @see #fillRectangle(ScreenBounds, Color)
     */
    default Screen fillRectangle(final Offset origin, final Size size, final Color color) {
        return fillRectangle(new ScreenBounds(origin, size), color);
    }

    /**
     * Takes a sceenshot of the whole {@link Screen}. This operation is very slow and will propably cause a small lag.
     * The screenshot may also include other applications that are in front of your game screen.
     */
    Sprite takeScreenshot();

    Screen drawFadingCircle(Offset offset, int diameter, Color color);

    Screen drawCircle(Offset offset, int diameter, Color color, int strokeWidth);

    default Screen drawCircle(Offset offset, int diameter, Color color) {
        return drawCircle(offset, diameter, color, 1);
    }

    Screen fillCircle(Offset offset, int diameter, Color color);

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
                      Flip flip, ScreenBounds clip);

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

    default Screen drawText(Offset offset, String text) {
        return drawText(offset, text, Pixelfont.defaultFont(), Percent.max());
    }

    default Screen drawText(Offset offset, String text, Pixelfont font, double scale) {
        return drawText(offset, text, font, Percent.max(), scale);
    }

    default Screen drawText(Offset offset, String text, double scale) {
        return drawText(offset, text, Pixelfont.defaultFont(), Percent.max(), scale);
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

    Screen drawText(Offset offset, String text, Font font, Color color);


    Screen drawTextCentered(final Offset position, final String text, final Font font, final Color color);

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

    /**
     * Returns {@code true} if the given {@link ScreenBounds} is within the{@link Screen} area.
     */
    boolean isVisible(ScreenBounds bounds);

    /**
     * Returns {@code true} if the given {@link Offset} is within the {@link Screen} area.
     */
    boolean isVisible(Offset offset);

    Screen drawRectangle(final Offset offset, final Size size, final Rotation rotation, final Color color);

    default Screen drawRectangle(final Offset offset, final Size size, final Color color) {
        return drawRectangle(offset, size, Rotation.none(), color);
    }
}
