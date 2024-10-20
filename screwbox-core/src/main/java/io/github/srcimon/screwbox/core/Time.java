package io.github.srcimon.screwbox.core;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a specific {@link Time} after starting the {@link Engine}. Can
 * only be used to measure {@link Duration}s relative to other {@link Time}
 * instances. Loses meaning when shutting down the JVM.
 *
 * @see Duration
 */
public class Time implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Time ZERO = atNanos(0);

    /**
     * Different time units.
     */
    public enum Unit {
        HOURS(3_600_000_000_000L, "h"),
        MINUTES(60_000_000_000L, "m"),
        SECONDS(1_000_000_000L, "s"),
        MILLISECONDS(1_000_000L, "ms"),
        MICROSECONDS(1_000L, "µs"),
        NANOSECONDS(1L, "ns");

        private final long nanos;
        private final String shortName;

        Unit(final long nanos, final String shortName) {
            this.nanos = nanos;
            this.shortName = shortName;
        }

        /**
         * Count of nanoseconds per time unit.
         */
        public long nanos() {
            return nanos;
        }

        /**
         * Short name of the time unit.
         */
        public String shortName() {
            return shortName;
        }
    }

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
        return nanos / Unit.MILLISECONDS.nanos();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        final int result = 1;
        return prime * result + Long.hashCode(nanos);
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
     * Add the given value measured as {@link Unit}.
     */
    public Time add(final long value, final Unit unit) {
        return new Time(nanos + unit.nanos * value);
    }

    /**
     * Returns a new instance of {@link Time} after the current {@link Time}
     * instance.
     *
     * @see #addMillis(long)
     */
    public Time addSeconds(final long seconds) {
        return add(seconds, Unit.SECONDS);
    }

    /**
     * Returns a new instance of {@link Time} after the current {@link Time}
     * instance.
     *
     * @see #addSeconds(long)
     */
    public Time addMillis(final long milliseconds) {
        return add(milliseconds, Unit.MILLISECONDS);
    }

    /**
     * Specifies if this instance is after the given {@link Time} instance.
     */
    public boolean isAfter(final Time other) {
        return other.isSet() && nanos > other.nanos;
    }

    /**
     * Specifies if this instance is before the given {@link Time} instance.
     */
    public boolean isBefore(final Time other) {
        return other.isSet() && nanos < other.nanos;
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
