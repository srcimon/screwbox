package dev.screwbox.core.graphics.options;

import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.utils.Validate;

/**
 * Customize the drawing of lines.
 *
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

    public LineDrawOptions drawOrder(final int drawOrder) {
        return new LineDrawOptions(color, strokeWidth, drawOrder);
    }
}
