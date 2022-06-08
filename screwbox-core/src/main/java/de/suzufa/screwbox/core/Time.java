package de.suzufa.screwbox.core;

import java.io.Serializable;

/**
 * Represents a specific {@link Time} after starting the {@link Engine}. Can
 * only be used to measure {@link Duration}s relative to other {@link Time}
 * instances. Loses meaning when shutting down the JVM.
 * 
 * @see Duration
 */
public class Time implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Time ZERO = atNanos(0);

    /**
     * Count of nanoseconds per second.
     */
    public static final long NANOS_PER_SECOND = 1_000_000_000;

    /**
     * Count of nanoseconds per millisecond.
     */
    public static final long NANOS_PER_MILLISECOND = 1_000_000;

    private final long nanos;

    /**
     * Returns the a new instance of the oldest possible {@link Time}.
     */
    public static Time unset() {
        return ZERO;
    }

    /**
     * Returns a new instance for the current {@link Time}.
     */
    public static Time now() {
        return new Time(System.nanoTime());
    }

    /**
     * Returns the a new instance for a specific {@link Time} whithin the JVM. Can
     * be handy for testing purposes.
     */
    public static Time atNanos(final long nanos) {
        return new Time(nanos);
    }

    private Time(final long nanos) {
        this.nanos = nanos;
    }

    /**
     * The nanoseconds value that represents the current {@link Time}.
     */
    public long nanos() {
        return nanos;
    }

    /**
     * The seconds value that represents the current {@link Time}.
     */
    public long milliseconds() {
        return nanos / NANOS_PER_MILLISECOND;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        return prime * result + (int) (nanos ^ (nanos >>> 32));
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Time other = (Time) obj;
        return nanos == other.nanos;
    }

    /**
     * Returns a new instance of {@link Time} after the current {@link Time}
     * instance.
     * 
     * @see #plusMillis(long)
     * @see #plus(Duration)
     */
    public Time plusSeconds(final long seconds) {
        return new Time(nanos + NANOS_PER_SECOND * seconds);
    }

    /**
     * Returns a new instance of {@link Time} after the current {@link Time}
     * instance.
     * 
     * @see #plusSeconds(long)
     * @see #plus(Duration)
     */
    public Time plusMillis(final long milliseconds) {
        return new Time(nanos + NANOS_PER_MILLISECOND * milliseconds);
    }

    /**
     * Specifies if this instance is after the given {@link Time} instance.
     */
    public boolean isAfter(final Time other) {
        return other.isSet() && nanos > other.nanos;
    }

    /**
     * Returns a new instance of {@link Time} after the current {@link Time}
     * instance.
     * 
     * @see #plusSeconds(long)
     * @see #plusMillis(long)
     */
    public Time plus(final Duration duration) {
        return new Time(nanos + duration.nanos());
    }

    @Override
    public String toString() {
        return "Time [nanos=" + nanos + "]";
    }

    /**
     * Returns {@code true} if this instance is unset.
     * 
     * @see #unset()
     */
    public boolean isUnset() {
        return nanos == 0;
    }

    /**
     * Returns {@code true} if this instance is not unset.
     * 
     * @see #unset()
     */
    public boolean isSet() {
        return !isUnset();
    }

}
