package dev.screwbox.core.graphics.options;

import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.utils.Validate;

import java.util.List;
import java.util.Objects;

/**
 * Specify options for drawing polygons.
 *
 * @param color          {@link Color} used to draw
 * @param secondaryColor secondary {@link Color} used to draw when using {@link Style#VERTICAL_GRADIENT}
 * @param style          {@link Style} used to draw
 * @param strokeWidth    stroke width when drawing {@link Style#OUTLINE}
 * @param smoothing      smoothing used for drawing
 * @see Canvas#drawPolygon(List, PolygonDrawOptions) .
 * @since 2.19.0
 */
public record PolygonDrawOptions(Color color, Color secondaryColor, Style style, int strokeWidth, Smoothing smoothing) {

    /**
     * The style used for drawing.
     */
    public enum Style {

        /**
         * Draws a filled form.
         */
        FILLED,

        /**
         * Draws only the outline using {@link PolygonDrawOptions#strokeWidth()}.
         */
        OUTLINE,

        /**
         * Fills the polygon using {@link #color()} and {@link #secondaryColor()}.
         */
        VERTICAL_GRADIENT
    }

    /**
     * Smoothing used for drawing the polygon.
     *
     * @since 3.14.0
     */
    public enum Smoothing {

        /**
         * No smoothing is applied on the polygon (default).
         */
        NONE,

        /**
         * Polygon will be smoothed horizontally. Edges won't be smoothed. Initially created to create fluid surfaces.
         */
        HORIZONTAL,

        /**
         * Polygon will be smoothed to create using spline algorithm.
         *
         * @since 3.14.0
         */
        SPLINE
    }

    public PolygonDrawOptions {
        Validate.positive(strokeWidth, "stroke width must be positive");
        Objects.requireNonNull(smoothing, "smoothing must not be null");
    }

    /**
     * Create a new instance using filled drawing style.
     */
    public static PolygonDrawOptions filled(final Color color) {
        return new PolygonDrawOptions(color, Color.TRANSPARENT, Style.FILLED, 1, Smoothing.NONE);
    }

    /**
     * Create a new instance using filled drawing style.
     */
    public static PolygonDrawOptions verticalGradient(final Color color, final Color secondaryColor) {
        return new PolygonDrawOptions(color, secondaryColor, Style.VERTICAL_GRADIENT, 1, Smoothing.NONE);
    }

    /**
     * Create a new instance using outline drawing style.
     */
    public static PolygonDrawOptions outline(final Color color) {
        return new PolygonDrawOptions(color, Color.TRANSPARENT, Style.OUTLINE, 1, Smoothing.NONE);
    }

    /**
     * Specify stroke width when drawing using {@link Style#OUTLINE}.
     */
    public PolygonDrawOptions strokeWidth(int strokeWidth) {
        return new PolygonDrawOptions(color, secondaryColor, style, strokeWidth, smoothing);
    }

    /**
     * Specify the smoothing used for drawing.
     */
    public PolygonDrawOptions smoothing(Smoothing smoothing) {
        return new PolygonDrawOptions(color, secondaryColor, style, strokeWidth, smoothing);
    }

}
