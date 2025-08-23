package dev.screwbox.core.graphics;

import dev.screwbox.core.utils.MathUtil;
import dev.screwbox.core.window.Window;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a Position on the {@link Window}.
 */
public final class Offset implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Offset ORIGIN = new Offset(0, 0);

    private final int x;
    private final int y;

    /**
     * Upper left corner of the {@link Window}.
     */
    public static Offset origin() {
        return ORIGIN;
    }

    /**
     * Creates a new {@link Offset} on the given coordinates.
     */
    public static Offset at(final double x, final double y) {
        return at((int) Math.round(x), (int) Math.round(y));
    }

    /**
     * Creates a new {@link Offset} on the given coordinates.
     */
    public static Offset at(final int x, final int y) {
        return new Offset(x, y);
    }

    private Offset(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * The x-coordinate of the {@link Offset}.
     */
    public int x() {
        return x;
    }

    /**
     * The y-coordinate of the {@link Offset}.
     */
    public int y() {
        return y;
    }

    @Override
    public String toString() {
        return "Offset [x=" + x + ", y=" + y + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Offset other = (Offset) obj;
        if (x != other.x)
            return false;
        return y == other.y;
    }

    public Offset substract(final Offset other) {
        return Offset.at(x - other.x, y - other.y);
    }

    public Offset addX(int x) {
        return add(x, 0);
    }

    public Offset addY(int y) {
        return add(0, y);
    }

    public Offset add(final Offset other) {
        return add(other.x, other.y);
    }

    public Offset add(final int x, final int y) {
        return x == 0 && y == 0
                ? this
                : Offset.at(this.x + x, this.y + y);
    }

    /**
     * Will snap the {@link Offset} to the specified grid size.
     * Will always move the {@link Offset} to the left and up when not already in grid.
     *
     * @since 3.4.0
     */
    public Offset snap(int gridSize) {
        return Offset.at(MathUtil.snapToGrid(x, gridSize), MathUtil.snapToGrid(y, gridSize));
    }

    /**
     * Will return the distance between the two {@link Offset offsets}.
     *
     * @since 3.8.0
     */
    public double distanceTo(final Offset other) {
        final double deltaX = x() - other.x;
        final double deltaY = y() - other.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
