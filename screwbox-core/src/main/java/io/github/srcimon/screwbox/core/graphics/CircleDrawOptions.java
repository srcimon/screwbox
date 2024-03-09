package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Vector;

import java.util.Objects;

/**
 * Customize the drawing of circles.
 *
 * @see Screen#drawCircle(Offset, int, CircleDrawOptions)
 * @see World#drawCircle(Vector, double, CircleDrawOptions)
 */
public class CircleDrawOptions {

    private final Style style;
    private final Color color;
    private int strokeWidth;

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

    private CircleDrawOptions(final Style style, final Color color) {
        this.style = style;
        this.color = color;
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
        if (style != Style.OUTLINE) {
            throw new IllegalArgumentException("stroke width is only used when drawing circle outline");
        }
        if (strokeWidth < 1) {
            throw new IllegalArgumentException("stroke width must be positive");
        }
        this.strokeWidth = strokeWidth;
        return this;
    }

    /**
     * The {@link Color} used to draw the circle.
     */
    public Color color() {
        return color;
    }

    /**
     * The {@link Style} used to draw the circle.
     */
    public Style style() {
        return style;
    }

    /**
     * Returns the stroke width used when drawing the outline of a circle.
     */
    public int strokeWidth() {
        return strokeWidth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CircleDrawOptions that = (CircleDrawOptions) o;

        if (strokeWidth != that.strokeWidth) return false;
        if (style != that.style) return false;
        return Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        int result = style != null ? style.hashCode() : 0;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + strokeWidth;
        return result;
    }

    @Override
    public String toString() {
        return "CircleDrawOptions{" +
                "style=" + style +
                ", color=" + color +
                ", strokeWidth=" + strokeWidth +
                '}';
    }
}
