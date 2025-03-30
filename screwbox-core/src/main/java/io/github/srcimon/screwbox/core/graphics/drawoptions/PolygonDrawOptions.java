package io.github.srcimon.screwbox.core.graphics.drawoptions;

import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.List;

/**
 * Specify options for drawing polygons.
 *
 * @param color       {@link Color} used to draw
 * @param style       {@link Style} used to draw
 * @param strokeWidth stroke width when drawing {@link Style#OUTLINE}
 * @see Canvas#drawPolygon(List, PolygonDrawOptions) .
 * @since 2.19.0
 */
public record PolygonDrawOptions(Color color, Style style, int strokeWidth) {

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
        OUTLINE
    }

    public PolygonDrawOptions {
        Validate.positive(strokeWidth, "stroke width must be positive");
    }

    /**
     * Create a new instance using filled drawing style.
     */
    public static PolygonDrawOptions filled(final Color color) {
        return new PolygonDrawOptions(color, Style.FILLED, 1);
    }

    /**
     * Create a new instance using outline drawing style.
     */
    public static PolygonDrawOptions outline(final Color color) {
        return new PolygonDrawOptions(color, Style.OUTLINE, 1);
    }

    /**
     * Specify stroke width when drawing using {@link Style#OUTLINE}.
     */
    public PolygonDrawOptions strokeWidth(int strokeWidth) {
        return new PolygonDrawOptions(color, style, strokeWidth);
    }

}
