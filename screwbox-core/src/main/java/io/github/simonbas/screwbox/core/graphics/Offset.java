package io.github.simonbas.screwbox.core.graphics;

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
        return Offset.at(this.x + x, y);
    }

    public Offset addY(int y) {
        return Offset.at(x, this.y + y);
    }

    public Offset add(final Offset other) {
        return Offset.at(x + other.x, y + other.y);
    }

}
