package dev.screwbox.core.utils;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.loop.Loop;

import java.io.Serial;
import java.io.Serializable;

/**
 * A simple scheduler to trigger timed actions.
 */
public class Scheduler implements Serializable {
//TODO use new short creation methods
    @Serial
    private static final long serialVersionUID = 1L;

    private final Duration interval;
    private Time nextTick;

    private Scheduler(final Duration interval) {
        this.interval = interval;
        this.nextTick = Time.unset();
    }

    /**
     * Creates a new {@link Scheduler} with the given interval.
     */
    public static Scheduler ofInterval(final Duration interval) {
        return new Scheduler(interval);
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
            nextTick = interval.addTo(time);
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
}
