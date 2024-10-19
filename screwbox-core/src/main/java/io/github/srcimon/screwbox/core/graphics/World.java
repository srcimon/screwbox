package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;

import java.util.function.Supplier;

public interface World {

    /**
     * Draws text on the {@link World} using {@link SystemTextDrawOptions}. Be warned: The used fonts are system specific and
     * drawing text is kind of slow. Text size does not change with {@link Camera#zoom()}.
     */
    World drawText(Vector position, String text, SystemTextDrawOptions options);

    /**
     * Draws a {@link Sprite} on the {@link World} using the given {@link SpriteDrawOptions}.
     */
    World drawSprite(Sprite sprite, Vector origin, SpriteDrawOptions options);

    /**
     * Draws a {@link Sprite} on the {@link World} using the given {@link SpriteDrawOptions}.
     */
    default World drawSprite(final Supplier<Sprite> sprite, final Vector origin, final SpriteDrawOptions options) {
        return drawSprite(sprite.get(), origin, options);
    }

    /**
     * Draws a sprite based text ({@link Pixelfont}) on the {@link World} using the given {@link TextDrawOptions}.
     * Text size changes with {@link Camera#zoom()}.
     */
    World drawText(Vector position, String text, TextDrawOptions options);

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
}