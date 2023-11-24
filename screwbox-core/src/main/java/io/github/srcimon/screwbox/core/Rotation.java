package io.github.srcimon.screwbox.core;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.util.Objects.requireNonNull;

/**
 * Represents the {@link Rotation} between two {@link Segment}s.
 */
public final class Rotation implements Serializable, Comparable<Rotation> {

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
     * @see #ofMomentum(Vector)
     */
    public static Rotation ofMomentum(final double x, final double y) {
        final double degrees = Math.toDegrees(Math.atan2(x, -1 * y));
        final double inRangeDegrees = degrees + Math.ceil(-degrees / 360) * 360;

        return Rotation.degrees(inRangeDegrees);
    }

    /**
     * Returns the {@link Rotation}-value of an objects momentum. This value equals the
     * angle between a vertical line and the {@link Vector} starting on the button
     * of this line.
     *
     * @see #ofMomentum(double, double)
     */
    public static Rotation ofMomentum(final Vector momentum) {
        return ofMomentum(momentum.x(), momentum.y());
    }

    /**
     * Creates a new {@link Rotation} of zero {@link #degrees()}.
     */
    public static Rotation none() {
        return NONE;
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
        return "Rotation [degrees=" + degrees + "]";
    }

    /**
     * Checks if there isn't any rotation.
     */
    public boolean isNone() {
        return degrees == MIN_VALUE;
    }

    /**
     * Returns the {@link Rotation} between a given {@link Segment} and a vertical line
     * from the {@link Segment#from()}.
     * 
     * @param segment the {@link Segment} that is used to calculate the {@link Rotation}
     */
    public static Rotation of(final Segment segment) {
        requireNonNull(segment, "segment must not be null");
        return ofMomentum(segment.to().substract(segment.from()));
    }

    /**
     * Rotates the given {@link Segment} by the given {@link Rotation} around
     * {@link Segment#from()}.
     * 
     * @param segment the {@link Segment} to be rotated
     * @return the rotated {@link Segment}
     */
    public Segment rotate(final Segment segment) {
        requireNonNull(segment, "segment must not be null");
        final double radians = radians();
        final double sinus = sin(radians);
        final double cosinus = cos(radians);
        final Vector translated = segment.to().substract(segment.from());
        final double xNew = translated.x() * cosinus - translated.y() * sinus + segment.from().x();
        final double yNew = translated.x() * sinus + translated.y() * cosinus + segment.from().y();

        return Segment.between(segment.from(), Vector.$(xNew, yNew));
    }

    @Override
    public int compareTo(final Rotation other) {
        return Double.compare(degrees, other.degrees);
    }
}
