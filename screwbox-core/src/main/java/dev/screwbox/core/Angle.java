package dev.screwbox.core;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

import static dev.screwbox.core.Vector.$;
import static dev.screwbox.core.utils.MathUtil.fastCos;
import static dev.screwbox.core.utils.MathUtil.fastSin;
import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.util.Objects.requireNonNull;

/**
 * Represents any {@link Angle} in degrees.
 */
public record Angle(double degrees) implements Serializable, Comparable<Angle> {

    private static final Random RANDOM = new Random();

    @Serial
    private static final long serialVersionUID = 1L;

    private static final double MIN_VALUE = 0;
    private static final double MAX_VALUE = 360;
    private static final Angle NONE = degrees(MIN_VALUE);

    /**
     * Creates a new {@link Angle} by the specified {@link #degrees()}.
     *
     * @see #degrees(double)
     */
    public Angle(final double degrees) {
        this.degrees = degrees % MAX_VALUE;
    }

    /**
     * Creates a new {@link Angle} by the specified {@link #degrees()}.
     */
    public static Angle degrees(final double degrees) {
        return new Angle(degrees);
    }

    /**
     * Creates a new {@link Angle} by the specified radians value.
     *
     * @since 3.18.0
     */
    //TODO add unit test
    public static Angle radians(final double radians) {
        return Angle.degrees(Math.toDegrees(radians));
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
        final double degrees = Math.toDegrees(atan2(x, -1 * y));
        final double inRangeDegrees = degrees + Math.ceil(-degrees / MAX_VALUE) * MAX_VALUE;

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
     * Calculates the {@link Angle} between two lines with the same origin, specified by three points.
     *
     * @since 3.17.0
     */
    public static Angle betweenLines(final Vector origin, final Vector firstEnd, final Vector secondEnd) {
        final double rad = atan2(firstEnd.y() - origin.y(), firstEnd.x() - origin.x()) -
                           atan2(secondEnd.y() - origin.y(), secondEnd.x() - origin.x());

        final double shortestRadians = rad < 0 ? rad + 2 * PI : rad;
        return Angle.radians(shortestRadians);
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
    @Override
    public double degrees() {
        return degrees;
    }

    /**
     * Returns the inverted {@link Angle} ( 360° - current rotation).
     */
    public Angle invert() {
        return Angle.degrees(MAX_VALUE - degrees);
    }

    /**
     * Returns {@code true}, if degrees of angle is zero.
     */
    public boolean isZero() {
        return degrees == MIN_VALUE;
    }

    /**
     * Returns the {@link Angle} between a given {@link Line} and a vertical line
     * from the {@link Line#start()}.
     *
     * @param line the {@link Line} that is used to calculate the {@link Angle}
     */
    public static Angle of(final Line line) {
        requireNonNull(line, "line must not be null");
        return ofVector(line.end().substract(line.start()));
    }

    //TODO document
    //TODO changelog
    //TODO unit test
    public static Angle ofLineBetweenPoints(final Vector start, final Vector end) {
        return ofVector(end.substract(start));
    }

    /**
     * Rotates the specified {@link Line} by the specified {@link Angle} around
     * {@link Line#start()}.
     *
     * @param line the {@link Line} to be rotated
     * @return the rotated {@link Line}
     */
    public Line applyOn(final Line line) {
        requireNonNull(line, "line must not be null");
        final var newEnd = rotatePointAroundCenter(line.end(), line.start());
        return Line.between(line.start(), newEnd);
    }

    //TODO document
    //TODO changelog
    //TODO test
    //TODO use whereever possible
    public Vector rotatePointAroundCenter(final Vector point, final Vector center) {
        final double radians = radians();
        final double sinus = fastSin(radians);
        final double cosinus = fastCos(radians);
        final double translatedX = point.x() - center.x();
        final double translatedY = point.y() - center.y();
        final double xNew = translatedX * cosinus - translatedY * sinus + center.x();
        final double yNew = translatedX * sinus + translatedY * cosinus + center.y();
        return $(xNew, yNew);
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
