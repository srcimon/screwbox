package io.github.srcimon.screwbox.core.graphics.options;

import io.github.srcimon.screwbox.core.graphics.Canvas;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.utils.Validate;

/**
 * Customize the drawing of lines.
 *
 * @see Canvas#drawLine(Offset, Offset, LineDrawOptions)
 */
public record LineDrawOptions(Color color, int strokeWidth) {

    public LineDrawOptions {
        Validate.positive(strokeWidth, "stroke width must be positive");
    }

    /**
     * Returns new instance with new {@link #color()}.
     */
    public static LineDrawOptions color(final Color color) {
        return new LineDrawOptions(color, 1);
    }

    /**
     * Returns new instance with new {@link #strokeWidth()}.
     */
    public LineDrawOptions strokeWidth(final int strokeWidth) {
        return new LineDrawOptions(color, strokeWidth);
    }
}
