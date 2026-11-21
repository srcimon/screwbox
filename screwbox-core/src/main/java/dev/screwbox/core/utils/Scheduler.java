package dev.screwbox.core.utils;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.loop.Loop;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

/**
 * A simple scheduler to trigger timed actions.
 */
public class Scheduler implements Serializable {

    private static final Random RANDOM = new Random();

    @Serial
    private static final long serialVersionUID = 1L;

    private final Duration interval;
    private final Duration jitter;
    private Time nextTick;

    private Scheduler(final Duration interval, final Duration jitter) {
        this.interval = interval;
        this.jitter = jitter;
        this.nextTick = Time.unset();
    }

    /**
     * Creates a {@link Scheduler} with random {@link #jitter()} for every tick.
     *
     * @since 3.41.0
     */
    public static Scheduler ofRandomMillis(final long minMillis, final long maxMillis) {
        return ofRandomInterval(Duration.ofMillis(minMillis), Duration.ofMillis(maxMillis));
    }

    /**
     * Creates a {@link Scheduler} with random {@link #jitter()} for every tick.
     *
     * @since 3.41.0
     */
    public static Scheduler ofRandomSeconds(final long minSeconds, final long maxSeconds) {
        return ofRandomInterval(Duration.ofSeconds(minSeconds), Duration.ofSeconds(maxSeconds));
    }

    /**
     * Creates a {@link Scheduler} with random {@link #jitter()} for every tick.
     *
     * @since 3.41.0
     */
    public static Scheduler ofRandomInterval(final Duration minInterval, final Duration maxInterval) {
        Validate.isTrue(maxInterval.isAtLeast(minInterval), "max interval must be at least or equal to min interval");

        return new Scheduler(
            Duration.ofNanos((long) ((minInterval.nanos() + maxInterval.nanos()) * 0.5)),
            Duration.ofNanos((long) ((maxInterval.nanos() - minInterval.nanos()) * 0.5)));
    }

    /**
     * Creates a new {@link Scheduler} with the given interval.
     */
    public static Scheduler ofInterval(final Duration interval) {
        return new Scheduler(interval, Duration.none());
    }

    /**
     * Creates a new {@link Scheduler} with the specified interval in minutes.
     */
    public static Scheduler ofMinutes(final long minutes) {
        return ofInterval(Duration.ofSeconds(60 * minutes));
    }

    /**
     * Creates a new {@link Scheduler} with the specified interval in seconds.
     */
    public static Scheduler ofSeconds(final long seconds) {
        return ofInterval(Duration.ofSeconds(seconds));
    }

    /**
     * Creates a new {@link Scheduler} with the specified interval in milliseconds.
     */
    public static Scheduler ofMillis(final long milliseconds) {
        return ofInterval(Duration.ofMillis(milliseconds));
    }

    /**
     * Checks if the timer is triggered at the given {@link Time}. If the timer is
     * then reseted. {@link Time} should be provided from outside to reduce CPU
     * load.
     *
     * @see #isTick()
     * @see Loop#time()
     */
    public boolean isTick(final Time time) {
        final boolean isNow = nextTick.isUnset() || time.isAfter(nextTick);
        if (isNow) {
            if (jitter.isNone()) {
                nextTick = interval.addTo(time);
            } else {
                final long nextJitterNanos = RANDOM.nextLong(-jitter.nanos(), jitter.nanos());
                nextTick = interval.add(Duration.ofNanos(nextJitterNanos)).addTo(time);
            }

        }
        return isNow;
    }

    /**
     * Checks if the timer is triggered now. This Method is much more CPU intensive
     * than {@link #isTick(Time)} because calculating nano time is CPU heavy.
     *
     * @see #isTick(Time)
     */
    public boolean isTick() {
        return isTick(Time.now());
    }

    /**
     * Returns the interval the {@link Scheduler} is using.
     */
    public Duration interval() {
        return interval;
    }

    /**
     * Returns the jitter in the {@link #interval()} the {@link Scheduler} is using.
     *
     * @since 3.41.0
     */
    public Duration jitter() {
        return jitter;
    }

    /**
     * Skips the {@link Scheduler#isTick() tick} until the specified time.
     *
     * @since 3.41.0
     */
    public void skipUntil(final Time time) {
        if (nextTick.isBefore(time)) {
            nextTick = time;
        }
    }
}
