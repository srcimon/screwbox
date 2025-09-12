package dev.screwbox.core.graphics.options;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.utils.Validate;

/**
 * Customize the drawing of circles.
 *
 * @param startAngle  start {@link Angle} when drawing arcs
 * @param arcAngle    {@link Angle} when drawing arcs
 * @param color       the {@link Color} used to draw
 * @param style       the style used to draw
 * @param strokeWidth stroke width for drawing outline circles
 * @see Canvas#drawCircle(Offset, int, CircleDrawOptions)
 * @see World#drawCircle(Vector, double, CircleDrawOptions)
 */
public record CircleDrawOptions(Style style, Color color, int strokeWidth, Angle arcAngle, Angle startAngle) {

    private CircleDrawOptions(final Style style, final Color color) {
        this(style, color, 1, Angle.none(), Angle.none());
    }

    public CircleDrawOptions {
        Validate.isTrue(() -> style != Style.OUTLINE && strokeWidth != 1, "stroke width is only used when drawing circle outline");
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
        return new CircleDrawOptions(Style.FILLED, color);
    }

    /**
     * Draw a fading circle with the given {@link Color}.
     */
    public static CircleDrawOptions fading(final Color color) {
        return new CircleDrawOptions(Style.FADING, color);
    }

    /**
     * Draw only the circle with the given {@link Color}.
     */
    public static CircleDrawOptions outline(final Color color) {
        return new CircleDrawOptions(Style.OUTLINE, color);
    }

    /**
     * Sets the {@link #strokeWidth()} when drawing {@link #outline(Color)}. Only used when using {@link #outline(Color)}.
     */
    public CircleDrawOptions strokeWidth(final int strokeWidth) {
        return new CircleDrawOptions(style, color, strokeWidth, arcAngle, startAngle);
    }

    /**
     * Sets the {@link Angle} when drawing arcs.
     *
     * @see #startAngle(Angle)
     * @since 3.9.0
     */
    public CircleDrawOptions arcAngle(final Angle arcAngle) {
        return new CircleDrawOptions(style, color, strokeWidth, arcAngle, startAngle);
    }

    /**
     * Sets the start {@link Angle} when drawing arcs.
     *
     * @see #arcAngle(Angle)
     * @since 3.9.0
     */
    public CircleDrawOptions startAngle(final Angle startAngle) {
        return new CircleDrawOptions(style, color, strokeWidth, arcAngle, startAngle);
    }
}
