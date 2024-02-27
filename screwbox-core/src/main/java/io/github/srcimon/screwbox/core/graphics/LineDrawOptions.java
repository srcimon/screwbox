package io.github.srcimon.screwbox.core.graphics;

import java.util.Objects;

/**
 * Customize the drawing of lines.
 *
 * @see Screen#drawLine(Offset, Offset, LineDrawOptions)
 */
public class LineDrawOptions {

    private final Color color;
    private int strokeWidth = 1;

    private LineDrawOptions(final Color color) {
        this.color = color;
    }

    /**
     * Sets the {@link Color} for drawing.
     */
    public static LineDrawOptions color(final Color color) {
        return new LineDrawOptions(color);
    }

    /**
     * Sets the stroke width for drawing.
     */
    public LineDrawOptions strokeWidth(final int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    /**
     * Returns the {@link Color} used when drawing the line.
     */
    public Color color() {
        return color;
    }

    /**
     * Returns the stroke width used when drawing the line.
     */
    public int strokeWidth() {
        return strokeWidth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LineDrawOptions that = (LineDrawOptions) o;

        if (strokeWidth != that.strokeWidth) return false;
        return Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        int result = color != null ? color.hashCode() : 0;
        result = 31 * result + strokeWidth;
        return result;
    }

    @Override
    public String toString() {
        return "LineDrawOptions{" +
                "color=" + color +
                ", strokeWidth=" + strokeWidth +
                '}';
    }
}
