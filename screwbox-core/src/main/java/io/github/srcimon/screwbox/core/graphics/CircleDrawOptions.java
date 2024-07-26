package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.utils.Validate;

/**
 * Customize the drawing of circles.
 *
 * @see Screen#drawCircle(Offset, int, CircleDrawOptions)
 * @see World#drawCircle(Vector, double, CircleDrawOptions)
 */
public record CircleDrawOptions(Style style, Color color, int strokeWidth) {

    public CircleDrawOptions {
        if (style != Style.OUTLINE && strokeWidth != 1) {
            throw new IllegalArgumentException("stroke width is only used when drawing circle outline");
        }
        Validate.positive(strokeWidth, "stroke width must be positive");
    }

    /**
     * The style used to draw.
     */
    public enum Style {

        /**
         * Draws a filled form.
         */
        FILLED,

        /**
         * Draws only the outline using {@link RectangleDrawOptions#strokeWidth()}.
         */
        OUTLINE,

        /**
         * Draws with {@link RectangleDrawOptions#color()} fading out from center of the circle.
         */
        FADING
    }


    /**
     * Draw a filled circle with the given {@link Color}.
     */
    public static CircleDrawOptions filled(final Color color) {
        return new CircleDrawOptions(Style.FILLED, color, 1);
    }

    /**
     * Draw a fading circle with the given {@link Color}.
     */
    public static CircleDrawOptions fading(final Color color) {
        return new CircleDrawOptions(Style.FADING, color, 1);
    }

    /**
     * Draw only the circle with the given {@link Color}.
     */
    public static CircleDrawOptions outline(final Color color) {
        return new CircleDrawOptions(Style.OUTLINE, color, 1);
    }

    /**
     * Sets the {@link #strokeWidth()} when drawing {@link #outline(Color)}. Only used when using {@link #outline(Color)}.
     */
    public CircleDrawOptions strokeWidth(final int strokeWidth) {
        return new CircleDrawOptions(style, color, strokeWidth);
    }
}
