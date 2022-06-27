package de.suzufa.screwbox.core.utils;

import static de.suzufa.screwbox.core.Time.now;

import java.io.Serializable;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.loop.Metrics;

/**
 * A simple timer to trigger timed actions.
 */
public class Timer implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Duration duration;
    private Time nextTick;

    private Timer(final Duration duration) {
        this.duration = duration;
        this.nextTick = Time.unset();
    }

    /**
     * Creates a new {@link Timer} with the given interval.
     */
    public static Timer withInterval(final Duration duration) {
        return new Timer(duration);
    }

    /**
     * Checks if the timer is triggered at the given {@link Time}. If the timer is
     * then reseted. {@link Time} should be provided from outside to reduce CPU
     * load.
     * 
     * @see #isTick()
     * @see Metrics#timeOfLastUpdate()
     */
    public boolean isTick(final Time time) {
        final boolean isNow = nextTick.isUnset() || time.isAfter(nextTick);
        if (isNow) {
            nextTick = time.plus(duration);
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
        return isTick(now());
    }
}
