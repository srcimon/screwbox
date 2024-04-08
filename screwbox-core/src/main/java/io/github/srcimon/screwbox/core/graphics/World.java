package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Vector;

import java.util.function.Supplier;

public interface World {

    /**
     * Draws text on the {@link World} using {@link SystemTextDrawOptions}. Be warned: The used fonts are system specific and
     * drawing text is kind of slow. Text size does not change with {@link Camera#zoom()}.
     */
    World drawText(Vector position, String text, SystemTextDrawOptions options);

    /**
     * Draws a {@link Sprite} on the {@link World} using the given {@link SpriteDrawOptions}.
     *
     * @see #drawSprite(Sprite, Vector, SpriteDrawOptions, Bounds)
     * @see #drawSpriteBatch(SpriteBatch)
     * @see #drawSpriteBatch(SpriteBatch, Bounds)
     */
    World drawSprite(Sprite sprite, Vector origin, SpriteDrawOptions options);

    /**
     * Draws a {@link Sprite} on the {@link World} using the given {@link SpriteDrawOptions}.
     *
     * @see #drawSprite(Sprite, Vector, SpriteDrawOptions, Bounds)
     * @see #drawSpriteBatch(SpriteBatch)
     * @see #drawSpriteBatch(SpriteBatch, Bounds)
     */
    default World drawSprite(Supplier<Sprite> sprite, Vector origin, SpriteDrawOptions options) {
        return drawSprite(sprite.get(), origin, options);
    }

    /**
     * Draws a {@link Sprite} on the {@link World} using the given {@link SpriteDrawOptions} but only draws on
     * the given {@link Bounds}. Can be used to apply effects.
     *
     * @see #drawSpriteBatch(SpriteBatch)
     * @see #drawSpriteBatch(SpriteBatch, Bounds)
     * @see #drawSprite(Sprite, Vector, SpriteDrawOptions)
     */
    World drawSprite(Sprite sprite, Vector origin, SpriteDrawOptions options, Bounds clip);

    /**
     * Draws a sprite based text ({@link Pixelfont}) on the {@link World} using the given {@link TextDrawOptions}.
     * Text size changes with {@link Camera#zoom()}.
     */
    World drawText(Vector position, String text, TextDrawOptions options);

    /**
     * Returns the area currently visible on the {@link Screen}.
     */
    Bounds visibleArea();

    /**
     * Draw a rectangle on the {@link World} using {@link RectangleDrawOptions}.
     */
    World drawRectangle(Bounds bounds, RectangleDrawOptions options);

    /**
     * Draw a {@link Line} on the {@link World} using {@link RectangleDrawOptions}.
     *
     * @see #drawLine(Line, LineDrawOptions)
     */
    World drawLine(Vector from, Vector to, LineDrawOptions options);

    /**
     * Draw a {@link Line} on the {@link World} using {@link RectangleDrawOptions}.
     *
     * @see #drawLine(Vector, Vector, LineDrawOptions)
     */
    default World drawLine(final Line line, final LineDrawOptions options) {
        return drawLine(line.from(), line.to(), options);
    }

    /**
     * Draw a circle on the {@link World} using {@link CircleDrawOptions}.
     */
    World drawCircle(Vector position, double radius, CircleDrawOptions options);

    /**
     * Draws a {@link SpriteBatch} (multiple ordered {@link Sprite}s) on the {@link World}.
     *
     * @see #drawSpriteBatch(SpriteBatch, Bounds)
     * @see #drawSprite(Sprite, Vector, SpriteDrawOptions)
     * @see #drawSprite(Sprite, Vector, SpriteDrawOptions, Bounds)
     */
    World drawSpriteBatch(SpriteBatch spriteBatch);

    /**
     * Draws a {@link SpriteBatch} (multiple ordered {@link Sprite}s) on the {@link World} but only draws on the
     * given {@link Bounds}. Can be used to apply effects.
     *
     * @see #drawSpriteBatch(SpriteBatch)
     * @see #drawSprite(Sprite, Vector, SpriteDrawOptions)
     * @see #drawSprite(Sprite, Vector, SpriteDrawOptions, Bounds)
     */
    World drawSpriteBatch(SpriteBatch spriteBatch, Bounds clip);
}