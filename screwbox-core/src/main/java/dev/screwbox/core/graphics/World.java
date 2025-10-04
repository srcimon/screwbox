package dev.screwbox.core.graphics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Line;
import dev.screwbox.core.Path;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.options.CircleDrawOptions;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;
import dev.screwbox.core.graphics.options.TextDrawOptions;

import java.util.List;
import java.util.function.Supplier;

/**
 * Draw directly to the game world. Automatically supports split screen and screen translation.
 */
public interface World {

    /**
     * Draws a polygon on the {@link World} using the specified {@link PolygonDrawOptions}.
     *
     * @since 2.19.0
     */
    World drawPolygon(List<Vector> nodes, PolygonDrawOptions options);

    /**
     * Draws a polygon on the {@link World} using the specified {@link PolygonDrawOptions}.
     *
     * @since 2.19.0
     */
    default World drawPolygon(Path path, PolygonDrawOptions options) {
        return drawPolygon(path.nodes(), options);
    }

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
    World drawCircle(final Vector position, final double radius, final CircleDrawOptions options);

    //TODO add javadoc
    World drawOval(Vector position, double radiusX, double radiusY, CircleDrawOptions options);//TODO rename circle drawoptions
}