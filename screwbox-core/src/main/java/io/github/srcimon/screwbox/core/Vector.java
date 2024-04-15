package io.github.srcimon.screwbox.core;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * The {@link Vector} represents a position or a distance in the 2d world. The
 * coordinates cannot be changed once the {@link Vector} is created.
 */
public final class Vector implements Serializable {

    private static final Random RANDOM = new Random();
    private static final Vector ZERO = new Vector(0, 0);

    @Serial
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
    public static Vector x(final double x) {
        return new Vector(x, 0);
    }

    /**
     * Returns a new {@link Vector} with only an {@link Vector#y}-component.
     */
    public static Vector y(final double y) {
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
     * Returns a new {@link Vector} representing the subtraction of the current and
     * the given and the other {@link Vector}.
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
        return calculateLength(deltaX, deltaY);
    }

    /**
     * Checks if {@link Vector} is located on coordinates 0:0.
     */
    public boolean isZero() {
        return x == 0 && y == 0;
    }

    /**
     * Returns a new {@link Vector} in the same direction but at the given length.
     * If {@link #isZero()} returns {@link #zero()}.
     */
    public Vector length(final double length) {
        if (length < 0) {
            throw new IllegalArgumentException("length must not be negative");
        }
        if (isZero()) {
            return this;
        }
        double factor = length / length();
        return Vector.of(x * factor, y * factor);
    }

    //TODO ParticleOptions.randomBaseSpeed()

    /**
     * Returns a new {@link Vector} with random direction an the given length.
     */
    public static Vector random(final double length) {
        return  Vector.of(RANDOM.nextDouble(-1, 1), RANDOM.nextDouble(-1, 1)).length(length);
    }

    /**
     * Returns the length of the {@link Vector}.
     */
    public double length() {
        return calculateLength(x, y);
    }

    private double calculateLength(final double x, final double y) {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Finds the nearest {@link Vector} in a {@link List} of {@link Vector}s.
     * Returns null if {@link List} is empty.
     */
    public Vector nearestOf(final List<Vector> others) {
        if (others.isEmpty()) {
            return null;
        }
        double maxDistance = Double.MAX_VALUE;
        Vector nearest = null;
        for (var other : others) {
            final double distance = distanceTo(other);
            if (distance < maxDistance) {
                nearest = other;
                maxDistance = distance;
            }
        }
        return nearest;
    }
}
