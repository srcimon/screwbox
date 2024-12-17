package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteFillOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;

import java.util.function.Supplier;

public interface Canvas extends Sizeable {

    /**
     * Returns the left upper edge of the {@link Canvas}.
     */
    Offset offset();

    /**
     * Returns the center of the {@link Canvas}.
     */
    Offset center();

    /**
     * Returns the {@link ScreenBounds} of the canvas.
     */
    ScreenBounds bounds();

    /**
     * Checks if the specified bounds is visible within the {@link Canvas}.
     */
    boolean isVisible(final ScreenBounds other);

    /**
     * Fills the whole {@link Canvas} with the given {@link Color}.
     */
    Canvas fillWith(Color color);

    /**
     * Fills the {@link Canvas} with a repeated {@link Sprite} using the given {@link SpriteFillOptions}.
     */
    Canvas fillWith(Sprite sprite, SpriteFillOptions options);

    /**
     * Draws text on the {@link Canvas} using {@link SystemTextDrawOptions}. Be warned: The used fonts are system specific and
     * drawing text is kind of slow.
     */
    Canvas drawText(Offset offset, String text, SystemTextDrawOptions options);

    /**
     * Draws a rectangle on the screen using the given {@link ScreenBounds} and {@link RectangleDrawOptions}.
     *
     * @see #drawRectangle(ScreenBounds, RectangleDrawOptions)
     */
    Canvas drawRectangle(Offset offset, Size size, RectangleDrawOptions options);

    /**
     * Draws a rectangle on the {@link Canvas} using the given {@link ScreenBounds} and {@link RectangleDrawOptions}.
     *
     * @see #drawRectangle(Offset, Size, RectangleDrawOptions)
     */
    default Canvas drawRectangle(final ScreenBounds bounds, final RectangleDrawOptions options) {
        return drawRectangle(bounds.offset(), bounds.size(), options);
    }

    /**
     * Draws a line on the {@link Canvas} using the given {@link Offset}s and {@link LineDrawOptions}.
     */
    Canvas drawLine(Offset from, Offset to, LineDrawOptions options);

    /**
     * Draws a circle on the {@link Canvas} using the given position and {@link CircleDrawOptions}.
     */
    Canvas drawCircle(Offset offset, int radius, CircleDrawOptions options);

    /**
     * Draws a {@link Sprite} on the {@link Canvas} using the given origin and {@link SpriteDrawOptions}.
     *
     * @see #drawSprite(Sprite, Offset, SpriteDrawOptions)
     */
    Canvas drawSprite(Supplier<Sprite> sprite, Offset origin, SpriteDrawOptions options);

    /**
     * Draws a {@link Sprite} on the {@link Canvas} using the given origin and {@link SpriteDrawOptions}.
     *
     * @see #drawSprite(Supplier, Offset, SpriteDrawOptions)
     */
    Canvas drawSprite(Sprite sprite, Offset origin, SpriteDrawOptions options);

    /**
     * Draws a sprite based text ({@link Pixelfont}) on the {@link Canvas} using the given {@link TextDrawOptions}.
     */
    Canvas drawText(Offset offset, String text, TextDrawOptions options);

    /**
     * Draws multiple sorted {@link Sprite sprites} at once. May have slightly better performance than drawing them
     * one by one.
     *
     * @since 1.11.0
     */
    Canvas drawSpriteBatch(SpriteBatch spriteBatch);

}
