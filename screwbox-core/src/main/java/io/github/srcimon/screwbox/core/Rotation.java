package io.github.srcimon.screwbox.core;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.util.Objects.requireNonNull;

/**
 * Represents the {@link Rotation} between two {@link Line}s.
 */
public final class Rotation implements Serializable, Comparable<Rotation> {

    private static final Random RANDOM = new Random();

    @Serial
    private static final long serialVersionUID = 1L;

    private static final double MIN_VALUE = 0;
    private static final double MAX_VALUE = 360;
    private static final Rotation NONE = degrees(MIN_VALUE);

    private final double degrees;

    private Rotation(final double degrees) {
        this.degrees = degrees % MAX_VALUE;
    }

    /**
     * Creates a new {@link Rotation} by the given {@link #degrees()}.
     */
    public static Rotation degrees(final double degrees) {
        return new Rotation(degrees);
    }

    /**
     * Returns the {@link Rotation}-value of an objects momentum. This value equals the
     * angle between a vertical line and the {@link Vector} starting on the button
     * of this line.
     *
     * @see #ofMovement(Vector)
     */
    public static Rotation ofMovement(final double x, final double y) {
        final double degrees = Math.toDegrees(Math.atan2(x, -1 * y));
        final double inRangeDegrees = degrees + Math.ceil(-degrees / 360) * 360;

        return Rotation.degrees(inRangeDegrees);
    }

    /**
     * Returns the {@link Rotation}-value of an objects momentum. This value equals the
     * angle between a vertical line and the {@link Vector} starting on the button
     * of this line.
     *
     * @see #ofMovement(double, double)
     */
    public static Rotation ofMovement(final Vector momentum) {
        return ofMovement(momentum.x(), momentum.y());
    }

    /**
     * Creates a new {@link Rotation} of zero {@link #degrees()}.
     */
    public static Rotation none() {
        return NONE;
    }

    /**
     * Creates a new random {@link Rotation}.
     */
    public static Rotation random() {
        return Rotation.degrees(RANDOM.nextInt(0, 360));
    }

    /**
     * Returns the radians value of this {@link Rotation}.
     */
    public double radians() {
        return Math.toRadians(degrees);
    }

    /**
     * Returns the degrees value of this {@link Rotation}.
     */
    public double degrees() {
        return degrees;
    }

    /**
     * Returns the inverted {@link Rotation} ( 360° - current rotation).
     */
    public Rotation invert() {
        return Rotation.degrees(360 - degrees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(degrees);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Rotation other = (Rotation) obj;
        return Double.doubleToLongBits(degrees) == Double.doubleToLongBits(other.degrees);
    }

    @Override
    public String toString() {
        return "Rotation [" + degrees + "°]";
    }

    /**
     * Checks if there isn't any rotation.
     */
    public boolean isNone() {
        return degrees == MIN_VALUE;
    }

    /**
     * Returns the {@link Rotation} between a given {@link Line} and a vertical line
     * from the {@link Line#from()}.
     *
     * @param line the {@link Line} that is used to calculate the {@link Rotation}
     */
    public static Rotation of(final Line line) {
        requireNonNull(line, "line must not be null");
        return ofMovement(line.to().substract(line.from()));
    }

    /**
     * Rotates the given {@link Line} by the given {@link Rotation} around
     * {@link Line#from()}.
     *
     * @param line the {@link Line} to be rotated
     * @return the rotated {@link Line}
     */
    public Line applyOn(final Line line) {
        requireNonNull(line, "line must not be null");
        final double radians = radians();
        final double sinus = sin(radians);
        final double cosinus = cos(radians);
        final Vector translated = line.to().substract(line.from());
        final double xNew = translated.x() * cosinus - translated.y() * sinus + line.from().x();
        final double yNew = translated.x() * sinus + translated.y() * cosinus + line.from().y();

        return Line.between(line.from(), Vector.$(xNew, yNew));
    }

    /**
     * Returns a new instance with the sum of this and the other {@link Rotation rotations} degrees.
     */
    public Rotation add(final Rotation other) {
        return Rotation.degrees(degrees + other.degrees);
    }

    @Override
    public int compareTo(final Rotation other) {
        return Double.compare(degrees, other.degrees);
    }
}
