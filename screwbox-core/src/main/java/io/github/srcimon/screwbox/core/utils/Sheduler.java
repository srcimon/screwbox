package io.github.srcimon.screwbox.core.utils;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.loop.Loop;

import java.io.Serializable;

/**
 * A simple sheduler to trigger timed actions.
 */
public class Sheduler implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Duration interval;
    private Time nextTick;

    private Sheduler(final Duration interval) {
        this.interval = interval;
        this.nextTick = Time.unset();
    }

    /**
     * Creates a new {@link Sheduler} with the given interval.
     */
    public static Sheduler withInterval(final Duration interval) {
        return new Sheduler(interval);
    }

    /**
     * Creates a new {@link Sheduler} with one minute interval.
     */
    public static Sheduler everyMinute() {
        return withInterval(Duration.ofSeconds(60));
    }

    /**
     * Creates a new {@link Sheduler} with one second interval.
     */
    public static Sheduler everySecond() {
        return withInterval(Duration.ofSeconds(1));
    }

    /**
     * Checks if the timer is triggered at the given {@link Time}. If the timer is
     * then reseted. {@link Time} should be provided from outside to reduce CPU
     * load.
     *
     * @see #isTick()
     * @see Loop#lastUpdate()
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
     * Returns the interval the {@link Sheduler} is using.
     */
    public Duration interval() {
        return interval;
    }
}
