package dev.screwbox.core;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

/**
 * A {@link Line} between two {@link Vector vectors}.
 */
public final class Line implements Serializable, Comparable<Line> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Vector start;
    private final Vector end;

    /**
     * Creates a new instance of {@link Line} defined by the two endpoints.
     */
    public static Line between(final Vector start, final Vector end) {
        return new Line(start, end);
    }

    /**
     * Creates a new instance of {@link Line} defined by the starting point and the length of the line going up from this point.
     */
    public static Line normal(final Vector start, final double length) {
        return between(start, start.addY(length));
    }

    private Line(final Vector start, final Vector end) {
        this.start = start;
        this.end = end;
    }

    /**
     * The starting point of the {@link Line}.
     */
    public Vector start() {
        return start;
    }

    /**
     * The endpoint of the {@link Line}.
     */
    public Vector end() {
        return end;
    }

    /**
     * Checks if the two specified {@link Line}s intersect.
     *
     * @see Line#intersectionPoint(Line)
     */
    public boolean intersects(final Line other) {
        //TODO add tests
        if (other.start.isSame(end) || other.end.equals(end) || other.start.isSame(start) || other.end.equals(start)) {
            return true;
        }

        final double xDelta = end.x() - start.x();
        final double yDelta = end.y() - start.y();
        final double fromToXDelta = other.end.x() - other.start.x();
        final double fromToYDelta = other.end.y() - other.start.y();
        final double nominator = xDelta * fromToYDelta - fromToXDelta * yDelta;

        if (nominator == 0) {
            return false;
        }
        final boolean nominatorIsPositive = nominator > 0;
        final double thisOtherXDelta = start.x() - other.start.x();
        final double thisOtherYDelta = start.y() - other.start.y();

        final double nominatorB = xDelta * thisOtherYDelta - yDelta * thisOtherXDelta;
        if (((nominatorB <= 0) == nominatorIsPositive || (nominatorB >= nominator) == nominatorIsPositive)) {
            return false;
        }

        final double nominatorC = fromToXDelta * thisOtherYDelta - fromToYDelta * thisOtherXDelta;
        return !((nominatorC <= 0) == nominatorIsPositive || (nominatorC >= nominator) == nominatorIsPositive);
    }

    /**
     * Returns the closest point on the line to the specified point.
     *
     * @since 3.17.0
     */
    public Vector closestPoint(final Vector point) {
        if (start.equals(end)) {
            return start;
        }

        final var deltaLine = end.substract(start);
        final var deltaStart = point.substract(start);

        final double normalizedDistance = (deltaStart.x() * deltaLine.x() + deltaStart.y() * deltaLine.y()) / (deltaLine.length() * deltaLine.length());

        if (normalizedDistance < 0.0) {
            return start;
        } else if (normalizedDistance > 1.0) {
            return end;
        }
        return Vector.of(
            start.x() + normalizedDistance * deltaLine.x(),
            start.y() + normalizedDistance * deltaLine.y());
    }

    /**
     * Returns the perpendicular {@link Line} from a specified point. Will be empty if there is no perpendicular from
     * the specified point.
     *
     * @since 3.22.0
     */
    public Optional<Line> perpendicular(final Vector point) {
        final var deltaLine = end.substract(start);
        final var deltaStart = point.substract(start);

        final double normalizedDistance = (deltaStart.x() * deltaLine.x() + deltaStart.y() * deltaLine.y()) / (deltaLine.length() * deltaLine.length());

        if (normalizedDistance < 0.0 || normalizedDistance > 1.0) {
            return Optional.empty();
        }
        final Vector pointOnLine = Vector.of(
            start.x() + normalizedDistance * deltaLine.x(),
            start.y() + normalizedDistance * deltaLine.y());

        return Optional.of(Line.between(pointOnLine, point));
    }

    /**
     * Finds all intersections between this {@link Line} and the given {@link Line}s.
     */
    public List<Vector> intersections(final List<Line> others) {
        final List<Vector> intersections = new ArrayList<>();
        for (final var other : others) {
            final var intersectionPoint = intersectionPoint(other);
            if (nonNull(intersectionPoint)) {
                intersections.add(intersectionPoint);
            }
        }
        return intersections;
    }

    //TODO document, changelog, test
    //TODO use whereever possible
    public Optional<Vector> closestIntersectionToStart(final List<Line> others) {
        double minDistance = Double.MAX_VALUE;
        Vector closestPoint = null;
        for (final var other : others) {
            final var intersectionPoint = intersectionPoint(other);
            if (nonNull(intersectionPoint)) {
                var distance = start.distanceTo(intersectionPoint);
                if(distance < minDistance) {
                    minDistance = distance;
                    closestPoint = intersectionPoint;
                }
            }
        }
        return Optional.ofNullable(closestPoint);
    }

    /**
     * Returns the intersection point of this and the other {@link Line}. Returns
     * null if there is no intersection.
     *
     * @see Line#intersects(Line)
     */
    public Vector intersectionPoint(final Line other) {
        //TODO add tests
        if (other.start.isSame(end) || other.end.equals(end)) {
            return end;
        }
        if (other.start.isSame(start) || other.end.equals(start)) {
            return start;
        }
        final double xDelta = end.x() - start.x();
        final double yDelta = end.y() - start.y();
        final double fromToXDelta = other.end.x() - other.start.x();
        final double fromToYDelta = other.end.y() - other.start.y();
        final double nominator = xDelta * fromToYDelta - fromToXDelta * yDelta;

        if (nominator == 0) {
            return null;
        }
        final boolean nominatorIsPositive = nominator > 0;
        final double thisOtherXDelta = start.x() - other.start.x();
        final double thisOtherYDelta = start.y() - other.start.y();

        final double nominatorB = xDelta * thisOtherYDelta - yDelta * thisOtherXDelta;
        if (((nominatorB <= 0) == nominatorIsPositive || (nominatorB >= nominator) == nominatorIsPositive)) {
            return null;
        }

        final double nominatorC = fromToXDelta * thisOtherYDelta - fromToYDelta * thisOtherXDelta;
        if (((nominatorC <= 0) == nominatorIsPositive || (nominatorC >= nominator) == nominatorIsPositive)) {
            return null;
        }

        final double collisionFactor = nominatorC / nominator;
        final double pX = start.x() + collisionFactor * xDelta;
        final double pY = start.y() + collisionFactor * yDelta;
        return Vector.of(pX, pY);
    }

    /**
     * Returns the point in the center of the line.
     */
    public Vector center() {
        return Vector.of(
            start.x() + (end.x() - start.x()) / 2.0,
            start.y() + (end.y() - start.y()) / 2.0);
    }

    @Override
    public String toString() {
        return "Line [start=" + start + ", end=" + end + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        result = prime * result + ((end == null) ? 0 : end.hashCode());
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
        final Line other = (Line) obj;
        if (start == null) {
            if (other.start != null)
                return false;
        } else if (!start.equals(other.start))
            return false;
        if (end == null) {
            return other.end == null;
        } else return end.equals(other.end);
    }

    /**
     * Returns the length of the line.
     */
    public double length() {
        return start.distanceTo(end);
    }

    @Override
    public int compareTo(final Line other) {
        return Double.compare(length(), other.length());
    }

}
