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
        return new Duration(micros * Time.NANOS_PER_MICROSECOND);
    }

    /**
     * Creates a new instance with the duration of the given value of milliseconds.
     * 
     * @see #ofNanos(long)
     * @see #ofSeconds(long)
     * @see #ofMicros(long)
     */
    public static Duration ofMillis(final long millis) {
        return new Duration(millis * Time.NANOS_PER_MILLISECOND);
    }

    /**
     * Creates a new instance with the duration of the given value of seconds.
     * 
     * @see #ofMillis(long)
     * @see #ofNanos(long)
     * @see #ofMicros(long)
     */
    public static Duration ofSeconds(final long seconds) {
        return new Duration(seconds * Time.NANOS_PER_SECOND);
    }

    public static Duration since(final Time time) {
        return new Duration(Time.now().nanos() - time.nanos());
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

    public long milliseconds() {
        return nanos / Time.NANOS_PER_MILLISECOND;
    }

    public long nanos() {
        return nanos;
    }

    public static Duration none() {
        return new Duration(0);
    }

    public Duration plus(final Duration other) {
        return Duration.ofNanos(nanos + other.nanos);
    }

    public static Duration between(final Time aTime, final Time anotherTime) {
        final long nanosBetween = abs(aTime.nanos() - anotherTime.nanos());
        return Duration.ofNanos(nanosBetween);
    }

    @Override
    public String toString() {
        return "Duration [nanos=" + nanos + "]";
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

    public boolean isAtLeast(final Duration other) {
        return nanos >= other.nanos();
    }

    public boolean isLessThan(final Duration other) {
        return !isAtLeast(other);
    }

}
