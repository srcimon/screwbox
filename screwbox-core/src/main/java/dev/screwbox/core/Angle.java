package dev.screwbox.core;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

import static dev.screwbox.core.Vector.$;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.util.Objects.requireNonNull;

/**
 * Represents any {@link Angle} in degrees.
 */
public final class Angle implements Serializable, Comparable<Angle> {

    private static final Random RANDOM = new Random();

    @Serial
    private static final long serialVersionUID = 1L;

    private static final double MIN_VALUE = 0;
    private static final double MAX_VALUE = 360;
    private static final Angle NONE = degrees(MIN_VALUE);

    private final double degrees;

    private Angle(final double degrees) {
        this.degrees = degrees % MAX_VALUE;
    }

    /**
     * Creates a new {@link Angle} by the given {@link #degrees()}.
     */
    public static Angle degrees(final double degrees) {
        return new Angle(degrees);
    }

    /**
     * Returns the {@link Angle} corresponding to the specified percentage of a circle.
     *
     * @since 3.9.0
     */
    public static Angle circle(final Percent percent) {
        return degrees(percent.value() * MAX_VALUE);
    }

    /**
     * Returns the {@link Angle}-value of an objects velocity. This value equals the
     * angle between a vertical line and the {@link Vector} starting on the button
     * of this line.
     *
     * @see #ofVector(Vector)
     */
    public static Angle ofVector(final double x, final double y) {
        final double degrees = Math.toDegrees(Math.atan2(x, -1 * y));
        final double inRangeDegrees = degrees + Math.ceil(-degrees / 360) * 360;

        return Angle.degrees(inRangeDegrees);
    }

    /**
     * Returns the {@link Angle}-value of an objects velocity. This value equals the
     * angle between a vertical line and the {@link Vector} starting on the button
     * of this line.
     *
     * @see #ofVector(double, double)
     */
    public static Angle ofVector(final Vector vector) {
        return ofVector(vector.x(), vector.y());
    }

    /**
     * Creates a new {@link Angle} of zero {@link #degrees()}.
     */
    public static Angle none() {
        return NONE;
    }

    /**
     * Creates a new random {@link Angle}.
     */
    public static Angle random() {
        return Angle.degrees(RANDOM.nextInt(0, 360));
    }

    /**
     * Returns the radians value of this {@link Angle}.
     */
    public double radians() {
        return Math.toRadians(degrees);
    }

    /**
     * Returns the degrees value of this {@link Angle}.
     */
    public double degrees() {
        return degrees;
    }

    /**
     * Returns the inverted {@link Angle} ( 360° - current rotation).
     */
    public Angle invert() {
        return Angle.degrees(360 - degrees);
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
        final Angle other = (Angle) obj;
        return Double.doubleToLongBits(degrees) == Double.doubleToLongBits(other.degrees);
    }

    @Override
    public String toString() {
        return "Angle [" + degrees + "°]";
    }

    /**
     * Returns {@code true}, if degrees of angle is zero.
     */
    public boolean isZero() {
        return degrees == MIN_VALUE;
    }

    /**
     * Returns the {@link Angle} between a given {@link Line} and a vertical line
     * from the {@link Line#from()}.
     *
     * @param line the {@link Line} that is used to calculate the {@link Angle}
     */
    public static Angle of(final Line line) {
        requireNonNull(line, "line must not be null");
        return ofVector(line.to().substract(line.from()));
    }

    /**
     * Rotates the specified {@link Line} by the specified {@link Angle} around
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

        return Line.between(line.from(), $(xNew, yNew));
    }

    /**
     * Returns a new instance with the sum of this and the other {@link Angle rotations} degrees.
     *
     * @see #addDegrees(double)
     */
    public Angle add(final Angle other) {
        return addDegrees(other.degrees);
    }

    /**
     * Returns a new instance with the sum of this and the specified degrees.
     *
     * @see #add(Angle)
     * @since 2.14.0
     */
    public Angle addDegrees(final double degrees) {
        return Angle.degrees(this.degrees + degrees);
    }

    /**
     * Returns the shortest distance from this instance to another.
     *
     * @since 2.14.0
     */
    public Angle delta(final Angle other) {
        requireNonNull(other, "other must not be null");
        final double delta = other.degrees - degrees;
        if (delta < -(MAX_VALUE / 2.0)) {
            return Angle.degrees(delta + MAX_VALUE);
        }
        return Angle.degrees(delta > (MAX_VALUE / 2.0) ? delta - MAX_VALUE : delta);
    }

    @Override
    public int compareTo(final Angle other) {
        return Double.compare(degrees, other.degrees);
    }
}
