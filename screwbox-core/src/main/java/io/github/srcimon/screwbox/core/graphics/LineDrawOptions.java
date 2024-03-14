package io.github.srcimon.screwbox.core.graphics;

/**
 * Customize the drawing of lines.
 *
 * @see Screen#drawLine(Offset, Offset, LineDrawOptions)
 */
public record LineDrawOptions(Color color, int strokeWidth) {


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
