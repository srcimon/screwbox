package io.github.srcimon.screwbox.core;

import java.io.Serial;
import java.io.Serializable;

import static java.lang.Math.abs;
import static java.util.Objects.requireNonNull;

/**
 * Represents a {@link Duration} between two {@link Time}s.
 *
 * @see Time
 */
public class Duration implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final long nanos;

    private Duration(final long nanos) {
        this.nanos = nanos;
    }

    /**
     * Creates a new instance with the duration of the given value of nanoseconds.
     *
     * @see #ofMillis(long)
     * @see #ofSeconds(long)
     * @see #ofMicros(long)
     */
    public static Duration ofNanos(final long nanos) {
        return new Duration(nanos);
    }

    /**
     * Creates a new instance with the duration of the given value of microseconds.
     *
     * @see #ofMillis(long)
     * @see #ofSeconds(long)
     * @see #ofNanos(long)
     */
    public static Duration ofMicros(final long micros) {
        return new Duration(micros * Time.Unit.MICROSECONDS.nanos());
    }

    /**
     * Creates a new instance with the duration of the given value of milliseconds.
     *
     * @see #ofNanos(long)
     * @see #ofSeconds(long)
     * @see #ofMicros(long)
     */
    public static Duration ofMillis(final long millis) {
        return new Duration(millis * Time.Unit.MILLISECONDS.nanos());
    }

    /**
     * Creates a new instance with the duration of the given value of seconds.
     *
     * @see #oneSecond()
     * @see #ofMillis(long)
     * @see #ofNanos(long)
     * @see #ofMicros(long)
     */
    public static Duration ofSeconds(final long seconds) {
        return new Duration(seconds * Time.Unit.SECONDS.nanos());
    }


    /**
     * Creates a new instance with the duration of one second.
     *
     * @see #ofMillis(long)
     * @see #ofNanos(long)
     * @see #ofMicros(long)
     */
    public static Duration oneSecond() {
        return ofSeconds(1);
    }

    /**
     * Creates a new instance with the duration since the given {@link Time} value.
     *
     * @see #ofExecution(Runnable)
     */
    public static Duration since(final Time time) {
        return new Duration(Time.now().nanos() - time.nanos());
    }

    /**
     * Creates a new instance with no duration at all.
     *
     * @see #isNone()
     */
    public static Duration none() {
        return new Duration(0);
    }

    /**
     * Checks if duration has no length.
     */
    public boolean isNone() {
        return nanos == 0;
    }

    /**
     * Creates a new instance with the duration of the time it took to execute the
     * given {@link Runnable}.
     *
     * @param execution the execution that is measured
     */
    public static Duration ofExecution(final Runnable execution) {
        final Time before = Time.now();
        requireNonNull(execution, "execution must not be null").run();
        return since(before);
    }

    /**
     * Creates a new instance with the duration between two given {@link Time} values.
     */
    public static Duration between(final Time aTime, final Time anotherTime) {
        final long nanosBetween = abs(aTime.nanos() - anotherTime.nanos());
        return Duration.ofNanos(nanosBetween);
    }

    /**
     * Returns the total milliseconds of the {@link Duration}.
     *
     * @see #nanos()
     * @see #seconds()
     */
    public long milliseconds() {
        return nanos / Time.Unit.MILLISECONDS.nanos();
    }

    /**
     * Returns the total nanoseconds of the {@link Duration}.
     *
     * @see #milliseconds()
     * @see #seconds()
     */
    public long nanos() {
        return nanos;
    }

    /**
     * Returns the total seconds of the {@link Duration}.
     *
     * @see #nanos()
     * @see #milliseconds()
     */
    public long seconds() {
        return nanos / Time.Unit.SECONDS.nanos();
    }

    /**
     * Adds another {@link Duration} to this {@link Duration}.
     *
     * @param other the {@link Duration} to add
     * @return the resulting {@link Duration}
     */
    public Duration add(final Duration other) {
        requireNonNull(other, "duration must not be null");
        return Duration.ofNanos(nanos + other.nanos);
    }

    @Override
    public String toString() {
        return "Duration [" + humanReadable() + "]";
    }

    @Override
    public int hashCode() {
        return 31 + (int) (nanos ^ (nanos >>> 32));
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Duration other = (Duration) obj;
        return nanos == other.nanos;
    }

    /**
     * Returns {@code true} if the {@link Duration} is at least that long with the other {@link Duration}.
     */
    public boolean isAtLeast(final Duration other) {
        return nanos >= other.nanos();
    }

    /**
     * Returns {@code true} if the {@link Duration} is at less than the other {@link Duration}.
     */
    public boolean isLessThan(final Duration other) {
        return !isAtLeast(other);
    }

    /**
     * Adds this {@link Duration} on the given {@link Time}.
     */
    public Time addTo(final Time time) {
        return Time.atNanos(time.nanos() + nanos);
    }

    /**
     * Returns a human readable {@link String} of the {@link Duration}.
     */
    public String humanReadable() {
        String result = null;
        long remaining = nanos;
        for (final var unit : Time.Unit.values()) {
            double unitFloor = Math.floor(1.0 * remaining / unit.nanos());
            if (result != null) {
                if (unitFloor < 1) {
                    return result;
                }
                return String.format("%s, %.0f%s", result, unitFloor, unit.shortName());
            }
            if (unitFloor >= 1.0) {
                result = String.format("%.0f%s", unitFloor, unit.shortName());
                remaining = remaining - (long) (unitFloor * unit.nanos());
                if (remaining == 0) {
                    return result;
                }
            }
        }
        return nanos + "ns";
    }

    //TODO REUSE WHEN POSSIBLE
    //TODO CHANGELOG javadoc test
    public Percent progress(final Time started, final Time now) {
        final var timePassed = Duration.between(started, now);
        return Percent.of(timePassed.nanos / nanos);
    }
}
