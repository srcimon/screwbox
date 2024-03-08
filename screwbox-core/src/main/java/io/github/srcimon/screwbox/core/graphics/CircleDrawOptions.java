package io.github.srcimon.screwbox.core.graphics;

//TODO Javadoc and test
public class CircleDrawOptions {

    private final Style style;
    private final Color color;
    private int strokeWidth;

    enum Style {
        OUTLINE,
        FILLED,
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

    public Color color() {
        return color;
    }

    public Style style() {
        return style;
    }

    public int strokeWidth() {
        return strokeWidth;
    }
}
