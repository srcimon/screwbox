package io.github.srcimon.screwbox.core;

import java.io.Serial;
import java.io.Serializable;

/**
 * Defines the {@link Segment} between two {@link Vector}s.
 */
public final class Segment implements Serializable, Comparable<Segment> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Vector from;
    private final Vector to;

    /**
     * Creates a new instance of {@link Segment} defined by the two endpoints.
     */
    public static Segment between(final Vector from, final Vector to) {
        return new Segment(from, to);
    }

    private Segment(final Vector from, final Vector to) {
        this.from = from;
        this.to = to;
    }

    /**
     * The starting point of the {@link Segment}.
     */
    public Vector from() {
        return from;
    }

    /**
     * The endpoint of the {@link Segment}.
     */
    public Vector to() {
        return to;
    }

    /**
     * Checks if the two given {@link Segment}s intersect each other.
     *
     * @see Segment#intersectionPoint(Segment)
     */
    public boolean intersects(final Segment other) {
        return intersectionPoint(other) != null;
    }

    /**
     * Returns the intersection Point of this and the other {@link Segment}. Returns
     * null if there is no intersection.
     *
     * @see Segment#intersects(Segment)
     */
    public Vector intersectionPoint(final Segment other) {
        final double xDelta = to.x() - from.x();
        final double yDelta = to.y() - from.y();
        final double fromToXDelta = other.to.x() - other.from.x();
        final double fromToYDelta = other.to.y() - other.from.y();
        final double nominator = xDelta * fromToYDelta - fromToXDelta * yDelta;

        if (nominator == 0) {
            return null;
        }
        final boolean nominatorIsPositive = nominator > 0;
        final double thisOtherXDelta = from.x() - other.from.x();
        final double thisOtherYDelta = from.y() - other.from.y();

        final double nominatorB = xDelta * thisOtherYDelta - yDelta * thisOtherXDelta;
        if (((nominatorB <= 0) == nominatorIsPositive || (nominatorB >= nominator) == nominatorIsPositive)) {
            return null;
        }

        final double nominatorC = fromToXDelta * thisOtherYDelta - fromToYDelta * thisOtherXDelta;
        if (((nominatorC <= 0) == nominatorIsPositive || (nominatorC >= nominator) == nominatorIsPositive)) {
            return null;
        }

        final double collisionFactor = nominatorC / nominator;
        final double pX = from.x() + (collisionFactor * xDelta);
        final double pY = from.y() + (collisionFactor * yDelta);
        return Vector.of(pX, pY);
    }

    @Override
    public String toString() {
        return "Segment [from=" + from + ", to=" + to + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((from == null) ? 0 : from.hashCode());
        result = prime * result + ((to == null) ? 0 : to.hashCode());
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
        final Segment other = (Segment) obj;
        if (from == null) {
            if (other.from != null)
                return false;
        } else if (!from.equals(other.from))
            return false;
        if (to == null) {
            if (other.to != null)
                return false;
        } else if (!to.equals(other.to))
            return false;
        return true;
    }

    public double length() {
        return from.distanceTo(to);
    }

    @Override
    public int compareTo(final Segment other) {
        return Double.compare(length(), other.length());
    }

}
