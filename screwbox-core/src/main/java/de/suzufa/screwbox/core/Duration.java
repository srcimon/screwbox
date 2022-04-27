package de.suzufa.screwbox.core;

import static java.lang.Math.abs;

import java.io.Serializable;

/**
 * Reresents a {@link Duration} between two {@link Time}s.
 *
 * @see Time
 */
public class Duration implements Serializable {

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
     */
    public static Duration ofNanos(final long nanos) {
        return new Duration(nanos);
    }

    /**
     * Creates a new instance with the duration of the given value of milliseconds.
     * 
     * @see #ofNanos(long)
     * @see #ofSeconds(long)
     */
    public static Duration ofMillis(final long millis) {
        return new Duration(millis * Time.NANOS_PER_MILLISECOND);
    }

    /**
     * Creates a new instance with the duration of the given value of seconds.
     * 
     * @see #ofMillis(long)
     * @see #ofNanos(long)
     */
    public static Duration ofSeconds(final long seconds) {
        return new Duration(seconds * Time.NANOS_PER_SECOND);
    }

    public static Duration since(final Time time) {
        return new Duration(Time.now().nanos() - time.nanos());
    }

    public long milliseconds() {
        return nanos / Time.NANOS_PER_MILLISECOND;
    }

    public long nanos() {
        return nanos;
    }

    public static Duration zero() {
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
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (nanos ^ (nanos >>> 32));
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
        final Duration other = (Duration) obj;
        if (nanos != other.nanos)
            return false;
        return true;
    }

    public boolean isAtLeast(final Duration other) {
        return nanos >= other.nanos();
    }

    public boolean isLessThan(final Duration other) {
        return !isAtLeast(other);
    }

}
