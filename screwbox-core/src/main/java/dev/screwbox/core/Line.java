package dev.screwbox.core;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        if (other.start.isSameAs(end) || other.end.isSameAs(end) || other.start.isSameAs(start) || other.end.isSameAs(start)) {
            return true;
        }
        final double startX = start.x();
        final double startY = start.y();
        final double endX = end.x();
        final double endY = end.y();

        final double otherStartX = other.start.x();
        final double otherStartY = other.start.y();
        final double otherEndX = other.end.x();
        final double otherEndY = other.end.y();

        final double xDelta = endX - startX;
        final double yDelta = endY - startY;
        final double fromToXDelta = otherEndX - otherStartX;
        final double fromToYDelta = otherEndY - otherStartY;
        final double nominator = xDelta * fromToYDelta - fromToXDelta * yDelta;

        if (nominator == 0.0) {
            return false;
        }

        final boolean nominatorIsPositive = nominator > 0.0;
        final double thisOtherXDelta = startX - otherStartX;
        final double thisOtherYDelta = startY - otherStartY;

        final double nominatorB = xDelta * thisOtherYDelta - yDelta * thisOtherXDelta;
        if (((nominatorB <= 0.0) == nominatorIsPositive || (nominatorB >= nominator) == nominatorIsPositive)) {
            return false;
        }

        final double nominatorC = fromToXDelta * thisOtherYDelta - fromToYDelta * thisOtherXDelta;
        return !((nominatorC <= 0.0) == nominatorIsPositive || (nominatorC >= nominator) == nominatorIsPositive);
    }

    /**
     * Returns the closest point on the line to the specified point.
     *
     * @since 3.17.0
     */
    public Vector closestPoint(final Vector point) {
        if (start.isSameAs(end)) {
            return start;
        }

        final var deltaLine = asVector();
        final var deltaStart = point.subtract(start);

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
        final var deltaLine = asVector();
        final var deltaStart = point.subtract(start);

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

    /**
     * Returns the intersection point of this and the other {@link Line}. Returns
     * null if there is no intersection.
     *
     * @see Line#intersects(Line)
     */
    public Vector intersectionPoint(final Line other) {
        if (other.start.isSameAs(end) || other.end.isSameAs(end)) {
            return end;
        }
        if (other.start.isSameAs(start) || other.end.isSameAs(start)) {
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

    /**
     * Moves the line by the specified value.
     *
     * @since 3.22.0
     */
    public Line move(final Vector movement) {
        return Line.between(start.add(movement), end.add(movement));
    }

    /**
     * Sets the length to the specified value. Moves only the {@link #end()}.
     *
     * @since 3.22.0
     */
    public Line length(final double distance) {
        return Line.between(start, start.add(end.subtract(start).length(distance)));
    }

    /**
     * Expands the lenght of the line in both directions.
     *
     * @since 3.23.0
     */
    public Line expand(final double length) {
        final var delta = asVector();
        final var x = delta.x() / delta.length() * length * 0.5;
        final var y = delta.y() / delta.length() * length * 0.5;
        return Line.between(start.add(-x, -y), end.add(x, y));
    }

    /**
     * Return {@code true} if position is left of line.
     *
     * @since 3.23.0
     */
    public boolean isLeft(final Vector position) {
        return calculateShoelaceOf(position) < 0;
    }

    /**
     * Return {@code true} if position is right of line.
     *
     * @since 3.23.0
     */
    public boolean isRight(final Vector position) {
        return calculateShoelaceOf(position) > 0;
    }

    /**
     * Return {@code true} if position is on the line
     *
     * @since 3.23.0
     */
    public boolean contains(final Vector position) {
        return calculateShoelaceOf(position) == 0;
    }

    private double calculateShoelaceOf(final Vector position) {
        return (end.x() - start.x()) * (position.y() - start.y()) -
               (end.y() - start.y()) * (position.x() - start.x());
    }

    /**
     * Bounces the {@link Line} of an obstacle and returns the resulting {@link Line} with the same length.
     * Does not require the {@link Line lines} to touch.
     *
     * @see <a href="https://www.sunshine2k.de/articles/coding/vectorreflection/vectorreflection.html">about vector reflection</a>
     * @since 3.30.0
     */
    public Line bounce(final Line obstacle) {
        Objects.requireNonNull(obstacle, "obstacle must not be null");

        final Vector incommingDirection = asVector().normalize();
        final Vector obstacleDirection = obstacle.asVector();

        Vector normalInIncommingDirection = Vector.of(-obstacleDirection.y(), obstacleDirection.x()).normalize();

        if (incommingDirection.dotProduct(normalInIncommingDirection) > 0) {
            normalInIncommingDirection = normalInIncommingDirection.invert();
        }

        final double dotProduct = incommingDirection.dotProduct(normalInIncommingDirection);
        final double rx = incommingDirection.x() - 2 * dotProduct * normalInIncommingDirection.x();
        final double ry = incommingDirection.y() - 2 * dotProduct * normalInIncommingDirection.y();

        final double length = length();
        return Line.between(end, end.add(rx * length, ry * length));
    }

    /**
     * Reverses the {@link Line} (switches {@link #start()} and {@link #end()}.
     *
     * @since 3.30.0
     */
    public Line reverse() {
        return Line.between(end, start);
    }

    /**
     * Returns the {@link Vector} representation of the {@link Line}.
     *
     * @since 3.30.0
     */
    public Vector asVector() {
        return end.subtract(start);
    }
}
