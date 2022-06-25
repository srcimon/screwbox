package de.suzufa.screwbox.core;

import java.io.Serializable;
import java.util.Locale;

/**
 * The {@link Vector} represents a position or a distance in the 2d world. The
 * coordinates cannot be changed once the {@link Vector} is created.
 */
public final class Vector implements Serializable {

    private static final Vector ZERO = new Vector(0, 0);

    private static final long serialVersionUID = 1L;

    private final double x;
    private final double y;

    /**
     * Creates a new Instance of {@link Vector} at coordinates 0:0.
     */
    public static Vector zero() {
        return ZERO;
    }

    /**
     * Creates a new Instance of {@link Vector} at the given coordinates. Short for
     * {@link #of(double, double)}
     */
    public static Vector $(final double x, final double y) {
        return new Vector(x, y);
    }

    /**
     * Creates a new Instance of {@link Vector} at the given coordinates.
     * 
     * @see #$(double, double)
     */
    public static Vector of(final double x, final double y) {
        return new Vector(x, y);
    }

    /**
     * Returns a {@link Vector} whose components are multiplied with the given
     * factor. Applicable when {@link Vector} is used as a speed or distance unit.
     * E.g. {@code speed.multiply(2)} returns double the speed.
     */
    public Vector multiply(final double factor) {
        return Vector.of(x * factor, y * factor);
    }

    /**
     * Returns a new {@link Vector} with only an {@link Vector#x}-component.
     */
    public static Vector xOnly(final double x) {
        return new Vector(x, 0);
    }

    /**
     * Returns a new {@link Vector} with only an {@link Vector#y}-component.
     */
    public static Vector yOnly(final double y) {
        return new Vector(0, y);
    }

    private Vector(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-component of the {@link Vector}.
     */
    public double x() {
        return x;
    }

    /**
     * Returns the y-component of the {@link Vector}.
     */
    public double y() {
        return y;
    }

    /**
     * Returns a new {@link Vector} representing the sum of the current and the
     * other {@link Vector}.
     */
    public Vector add(final Vector other) {
        return add(other.x, other.y);
    }

    /**
     * Returns a new {@link Vector} representing the sum of the current
     * {@link Vector} and the given x- and y-components.
     */
    public Vector add(final double x, final double y) {
        return Vector.of(this.x + x, this.y + y);
    }

    /**
     * Returns a new {@link Vector} representing the sum of the current
     * {@link Vector} and the given x-component.
     */
    public Vector addX(final double xDelta) {
        return Vector.of(x + xDelta, y);
    }

    /**
     * Returns a new {@link Vector} representing the sum of the current
     * {@link Vector} and the given y-component.
     */
    public Vector addY(final double yDelta) {
        return Vector.of(x, y + yDelta);
    }

    /**
     * Returns a new {@link Vector} representing the substraction of the current and
     * the given and the orther {@link Vector}.
     */
    public Vector substract(final Vector other) {
        return Vector.of(x - other.x, y - other.y);
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "Vector [x=%.2f, y=%.2f]", x, y);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        return prime * result + (int) (temp ^ (temp >>> 32));
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Vector other = (Vector) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
            return false;
        return Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y);
    }

    /**
     * Returns a new {@link Vector} with inverted X- and Y-components.
     */
    public Vector invert() {
        return Vector.of(-1 * x, -1 * y);
    }

    /**
     * Returns a new {@link Vector} with inverted X-component.
     */
    public Vector invertX() {
        return Vector.of(-1 * x, y);
    }

    /**
     * Returns a new {@link Vector} with inverted Y-component.
     */
    public Vector invertY() {
        return Vector.of(x, -1 * y);
    }

    /**
     * Calculates the distance between this and the other {@link Vector}.
     */
    public double distanceTo(final Vector other) {
        final double deltaX = x() - other.x;
        final double deltaY = y() - other.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Checks if {@link Vector} is located on coordinates 0:0.
     */
    public boolean isZero() {
        return x == 0 && y == 0;
    }

    // TODO: REFACTOR
    public Vector capToLength(double length) {
        double factor = length / Math.sqrt(x * x + y * y);// TODO: duplicate code
        return Vector.of(x * factor, y * factor);
    }

}
