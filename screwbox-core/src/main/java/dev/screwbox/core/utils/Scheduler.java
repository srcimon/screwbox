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
    public static Scheduler withInterval(final Duration interval) {
        return new Scheduler(interval);
    }

    /**
     * Creates a new {@link Scheduler} with one minute interval.
     */
    public static Scheduler everyMinute() {
        return withInterval(Duration.ofSeconds(60));
    }

    /**
     * Creates a new {@link Scheduler} with one second interval.
     */
    public static Scheduler everySecond() {
        return withInterval(Duration.ofSeconds(1));
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
