package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Rotation;

import java.util.Objects;

//TODO javadoc and test
public class RectangleOptions {

    private final boolean isFilled;
    private final Color color;
    private int strokeWidth = 1;
    private Rotation rotation = Rotation.none();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RectangleOptions that = (RectangleOptions) o;

        if (isFilled != that.isFilled) return false;
        if (strokeWidth != that.strokeWidth) return false;
        if (!Objects.equals(color, that.color)) return false;
        return Objects.equals(rotation, that.rotation);
    }

    @Override
    public int hashCode() {
        int result = (isFilled ? 1 : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + strokeWidth;
        result = 31 * result + (rotation != null ? rotation.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RectangleOptions{" +
                "isFilled=" + isFilled +
                ", color=" + color +
                ", strokeWidth=" + strokeWidth +
                ", rotation=" + rotation +
                '}';
    }
}
