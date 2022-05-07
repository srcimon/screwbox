package de.suzufa.screwbox.core;

import java.util.Objects;

/**
 * Represents the Rotation of an Object.
 */
public final class Rotation {

    private static final double MIN_VALUE = 0;
    private static final double MAX_VALUE = 360;
    private static final Rotation NONE = ofDegrees(MIN_VALUE);

    private final double degrees;

    private Rotation(final double degrees) {
        this.degrees = degrees % MAX_VALUE;
    }

    /**
     * Creates a new Rotation by the given {@link #degrees()}.
     */
    public static Rotation ofDegrees(final double degrees) {
        return new Rotation(degrees);
    }

    /**
     * Creates a new Rotation of zero {@link #degrees()}.
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

    // TODO: CLEAN UP THIS MESS BELOW!!!!!!

    public static Rotation normalAngle(Vector target) {
        return Rotation.ofDegrees(calculateAngle(Vector.zero(), Vector.of(target.x(), -1.0 * target.y())));
    }

    public static double calculateAngle(Vector first, Vector second) {
        double angle = Math.toDegrees(Math.atan2(second.x() - first.x(), second.y() - first.y()));
        // Keep angle between 0 and 360
        angle = angle + Math.ceil(-angle / 360) * 360;

        return angle;
    }

}
