package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteFillOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;

import java.util.function.Supplier;

public interface Viewport {

    /**
     * Fills the whole {@link Viewport} with the given {@link Color}.
     */
    Viewport fillWith(Color color);

    /**
     * Draws a rectangle on the Viewport using the given {@link ScreenBounds} and {@link RectangleDrawOptions}.
     *
     * @see #drawRectangle(ScreenBounds, RectangleDrawOptions)
     */
    Viewport drawRectangle(final Offset origin, final Size size, final RectangleDrawOptions options);

    /**
     * Draws a rectangle on the {@link Viewport} using the given {@link ScreenBounds} and {@link RectangleDrawOptions}.
     *
     * @see #drawRectangle(Offset, Size, RectangleDrawOptions)
     */
    default Viewport drawRectangle(final ScreenBounds bounds, final RectangleDrawOptions options) {
        return drawRectangle(bounds.offset(), bounds.size(), options);
    }


    /**
     * Draws a line on the {@link Viewport} using the given {@link Offset}s and {@link LineDrawOptions}.
     */
    Viewport drawLine(Offset from, Offset to, LineDrawOptions options);

    /**
     * Draws a circle on the {@link Viewport} using the given position and {@link CircleDrawOptions}.
     */
    Viewport drawCircle(Offset offset, int radius, CircleDrawOptions options);

    /**
     * Draws a {@link Sprite} on the {@link Viewport} using the given origin and {@link SpriteDrawOptions}.
     *
     * @see #drawSprite(Sprite, Offset, SpriteDrawOptions)
     */
    Viewport drawSprite(Supplier<Sprite> sprite, Offset origin, SpriteDrawOptions options);

    /**
     * Draws a {@link Sprite} on the {@link Viewport} using the given origin and {@link SpriteDrawOptions}.
     *
     * @see #drawSprite(Supplier, Offset, SpriteDrawOptions)
     */
    Viewport drawSprite(Sprite sprite, Offset origin, SpriteDrawOptions options);

    /**
     * Draws text on the {@link Viewport} using {@link SystemTextDrawOptions}. Be warned: The used fonts are system specific and
     * drawing text is kind of slow.
     */
    Viewport drawText(Offset offset, String text, SystemTextDrawOptions options);

    /**
     * Draws a sprite based text ({@link Pixelfont}) on the {@link Viewport} using the given {@link TextDrawOptions}.
     */
    Viewport drawText(Offset offset, String text, TextDrawOptions options);

    /**
     * Fills the {@link Viewport} with a repeated {@link Sprite} using the given {@link SpriteFillOptions}.
     */
    Viewport fillWith(Sprite sprite, SpriteFillOptions options);

    /**
     * Draws multiple sorted {@link Sprite sprites} at once. May have slightly better performance than drawing them
     * one by one.
     *
     * @since 1.11.0
     */
    Viewport drawSpriteBatch(SpriteBatch spriteBatch);
}
