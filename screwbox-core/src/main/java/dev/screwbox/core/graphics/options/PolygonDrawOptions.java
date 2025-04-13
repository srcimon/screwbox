package dev.screwbox.core.graphics.options;

import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.utils.Validate;

import java.util.List;

/**
 * Specify options for drawing polygons.
 *
 * @param color                  {@link Color} used to draw
 * @param secondaryColor         secondary {@link Color} used to draw when using {@link Style#VERTICAL_GRADIENT}
 * @param style                  {@link Style} used to draw
 * @param strokeWidth            stroke width when drawing {@link Style#OUTLINE}
 * @param isSmoothenHorizontally smoothen the polygon horizontally
 * @see Canvas#drawPolygon(List, PolygonDrawOptions) .
 * @since 2.19.0
 */
public record PolygonDrawOptions(Color color, Color secondaryColor, Style style, int strokeWidth,
                                 boolean isSmoothenHorizontally) {

    /**
     * The style used to draw.
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

    public PolygonDrawOptions {
        Validate.positive(strokeWidth, "stroke width must be positive");
    }

    /**
     * Create a new instance using filled drawing style.
     */
    public static PolygonDrawOptions filled(final Color color) {
        return new PolygonDrawOptions(color, Color.TRANSPARENT, Style.FILLED, 1, false);
    }

    /**
     * Create a new instance using filled drawing style.
     */
    public static PolygonDrawOptions verticalGradient(final Color color, final Color secondaryColor) {
        return new PolygonDrawOptions(color, secondaryColor, Style.VERTICAL_GRADIENT, 1, false);
    }

    /**
     * Create a new instance using outline drawing style.
     */
    public static PolygonDrawOptions outline(final Color color) {
        return new PolygonDrawOptions(color, Color.TRANSPARENT, Style.OUTLINE, 1, false);
    }

    /**
     * Specify stroke width when drawing using {@link Style#OUTLINE}.
     */
    public PolygonDrawOptions strokeWidth(int strokeWidth) {
        return new PolygonDrawOptions(color, secondaryColor, style, strokeWidth, isSmoothenHorizontally);
    }

    /**
     * Smoothen the polygon horizontally.
     */
    public PolygonDrawOptions smoothenHorizontally() {
        return new PolygonDrawOptions(color, secondaryColor, style, strokeWidth, true);
    }

}
