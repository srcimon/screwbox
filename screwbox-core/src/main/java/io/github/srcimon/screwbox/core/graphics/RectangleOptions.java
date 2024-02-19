package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Rotation;

//TODO javadoc and test
public class RectangleOptions {

    private final boolean isFilled;
    private final Color color;
    private int strokeWidth;
    private Rotation rotation;

    private RectangleOptions(final boolean isFilled, final Color color) {
        this.isFilled = isFilled;
        this.color = color;
    }

    public static RectangleOptions filled(final Color color) {
        return new RectangleOptions(true, color);
    }

    public static RectangleOptions outline(final Color color) {
        return new RectangleOptions(false, color);
    }

    public RectangleOptions strokeWidth(final int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    public RectangleOptions rotation(final Rotation rotation) {
        this.rotation = rotation;
        return this;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public Color color() {
        return color;
    }

    public int strokeWidth() {
        return strokeWidth;
    }

    public Rotation rotation() {
        return rotation;
    }
}
