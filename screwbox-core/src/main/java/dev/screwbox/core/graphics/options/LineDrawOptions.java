package dev.screwbox.core.graphics.options;

import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.utils.Validate;

/**
 * Customize the drawing of lines.
 *
 * @param color       {@link Color} used for drawing the line
 * @param strokeWidth stroke width used for drawing the line
 * @param drawOrder   order of this drawing task in comparison to others
 * @see Canvas#drawLine(Offset, Offset, LineDrawOptions)
 */
public record LineDrawOptions(Color color, int strokeWidth, int drawOrder) {

    public LineDrawOptions {
        Validate.positive(strokeWidth, "stroke width must be positive");
    }

    /**
     * Returns new instance with new {@link #color()}.
     */
    public static LineDrawOptions color(final Color color) {
        return new LineDrawOptions(color, 1, 0);
    }

    /**
     * Returns new instance with new {@link #strokeWidth()}.
     */
    public LineDrawOptions strokeWidth(final int strokeWidth) {
        return new LineDrawOptions(color, strokeWidth, drawOrder);
    }

    /**
     * Specify the order of this drawing task in comparison to others.
     *
     * @since 3.14.0
     */
    public LineDrawOptions drawOrder(final int drawOrder) {
        return new LineDrawOptions(color, strokeWidth, drawOrder);
    }
}
