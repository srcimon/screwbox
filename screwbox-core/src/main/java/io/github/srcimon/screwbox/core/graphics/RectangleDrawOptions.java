package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Rotation;

import java.util.Objects;

/**
 * Customize the drawing of rectangles.
 *
 * @see Screen#drawRectangle(ScreenBounds, RectangleDrawOptions)
 * @see Screen#drawRectangle(Offset, Size, RectangleDrawOptions)
 * @see World#drawRectangle(Bounds, RectangleDrawOptions)
 */
public class RectangleDrawOptions {

    private final boolean isFilled;
    private final Color color;
    private int strokeWidth = 1;
    private Rotation rotation = Rotation.none();

    private RectangleDrawOptions(final boolean isFilled, final Color color) {
        this.isFilled = isFilled;
        this.color = color;
    }

    /**
     * Draw a filled rectangle with the given {@link Color}.
     */
    public static RectangleDrawOptions filled(final Color color) {
        return new RectangleDrawOptions(true, color);
    }

    /**
     * Draw only the outline with the given {@link Color}.
     */
    public static RectangleDrawOptions outline(final Color color) {
        return new RectangleDrawOptions(false, color);
    }

    /**
     * Sets the {@link #strokeWidth()} when drawing {@link #outline(Color)}. Not used when using {@link #filled(Color)}.
     */
    public RectangleDrawOptions strokeWidth(final int strokeWidth) {
        if (isFilled) {
            throw new IllegalArgumentException("stroke width is not used when drawing filled rectangles");
        }
        if (strokeWidth < 1) {
            throw new IllegalArgumentException("stroke width must be positive");
        }
        this.strokeWidth = strokeWidth;
        return this;
    }

    /**
     * Sets the {@link #rotation()} of the drawn rectangle.
     */
    public RectangleDrawOptions rotation(final Rotation rotation) {
        this.rotation = rotation;
        return this;
    }

    /**
     * Returns {@code true} if the rectangle is filled.
     */
    public boolean isFilled() {
        return isFilled;
    }

    /**
     * Returns the {@link Color} used when drawing the rectangle.
     */
    public Color color() {
        return color;
    }

    /**
     * Returns the stroke width used when drawing the rectangle.
     */
    public int strokeWidth() {
        return strokeWidth;
    }

    /**
     * Returns the {@link Rotation} used when drawing the rectangle.
     */
    public Rotation rotation() {
        return rotation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RectangleDrawOptions that = (RectangleDrawOptions) o;

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
