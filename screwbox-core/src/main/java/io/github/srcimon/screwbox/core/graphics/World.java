package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Vector;

import java.util.function.Supplier;

public interface World {

    //TODO javadoc and test
    World drawSprite(Sprite sprite, Vector origin, SpriteDrawOptions options);

    World drawSpriteBatch(SpriteBatch spriteBatch, Bounds clip);

    World drawText(Vector offset, String text, Font font, Color color);

    World drawTextCentered(Vector position, String text, Font font, Color color);

    World drawTextCentered(Vector position, String text, Pixelfont font, Percent opacity, double scale);

    default World drawTextCentered(final Vector position, final String text, final Pixelfont font,
                                   final Percent opacity) {
        return drawTextCentered(position, text, font, opacity, 1);
    }

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
}