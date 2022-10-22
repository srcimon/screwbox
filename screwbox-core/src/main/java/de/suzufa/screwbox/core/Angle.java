package de.suzufa.screwbox.core;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the {@link Angle} between two {@link Segment}s.
 */
public final class Angle implements Serializable, Comparable<Angle> {

    private static final long serialVersionUID = 1L;

    private static final double MIN_VALUE = 0;
    private static final double MAX_VALUE = 360;
    private static final Angle NONE = ofDegrees(MIN_VALUE);

    private final double degrees;

    private Angle(final double degrees) {
        this.degrees = degrees % MAX_VALUE;
    }

    /**
     * Creates a new {@link Angle} by the given {@link #degrees()}.
     */
    public static Angle ofDegrees(final double degrees) {
        return new Angle(degrees);
    }

    /**
     * Returns the {@link Angle}-value of an objects momentum. This value equals the
     * angle between a vertical line and the {@link Vector} starting on the button
     * of this line.
     * 
     * @see #ofMomentum(Vector)
     */
    public static Angle ofMomentum(final double x, final double y) {
        final double degrees = Math.toDegrees(Math.atan2(x, -1 * y));
        final double inRangeDegrees = degrees + Math.ceil(-degrees / 360) * 360;

        return Angle.ofDegrees(inRangeDegrees);
    }

    /**
     * Returns the {@link Angle}-value of an objects momentum. This value equals the
     * angle between a vertical line and the {@link Vector} starting on the button
     * of this line.
     * 
     * 
     * @see #ofMomentum(double, double)
     */
    public static Angle ofMomentum(final Vector momentum) {
        return ofMomentum(momentum.x(), momentum.y());
    }

    /**
     * Creates a new {@link Angle} of zero {@link #degrees()}.
     */
    public static Angle none() {
        return NONE;
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
        return "Angle [degrees=" + degrees + "]";
    }

    /**
     * Checks if there isn't any rotation.
     */
    public boolean isNone() {
        return degrees == MIN_VALUE;
    }

    /**
     * Returns the {@link Angle} between a given {@link Segment} and a vertical line
     * from the {@link Segment#from()}.
     * 
     * @param segment the {@link Segment} that is used to calculate the angle
     */
    public static Angle of(final Segment segment) {
        requireNonNull(segment, "segment must not be null");
        return ofMomentum(segment.to().substract(segment.from()));
    }

    /**
     * Rotates the given {@link Segment} by the given {@link Angle} around
     * {@link Segment#from()}.
     * 
     * @param segment the {@link Segment} to be rotated
     * @return the rotated {@link Segment}
     */
    public Segment rotate(final Segment segment) {
        requireNonNull(segment, "segment must not be null");
        final double s = sin(Angle.ofDegrees(degrees).radians());
        final double c = cos(Angle.ofDegrees(degrees).radians());
        final Vector translated = segment.to().substract(segment.from());
        final double xnew = translated.x() * c - translated.y() * s;
        final double ynew = translated.x() * s + translated.y() * c;

        return Segment.between(segment.from(), Vector.$(xnew, ynew).add(segment.from()));
    }

    @Override
    public int compareTo(final Angle other) {
        return Double.compare(degrees, other.degrees);
    }
}
