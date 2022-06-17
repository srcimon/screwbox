package de.suzufa.screwbox.core.utils;

import java.io.Serializable;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;

/**
 * A simple timer to trigger timed actions.
 */
public class Timer implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Duration duration;
    private Time nextTick;

    private Timer(final Duration duration) {
        this.duration = duration;
        this.nextTick = Time.now().plus(duration);
    }

    /**
     * Creates a new {@link Timer} with the given interval.
     */
    public static Timer withInterval(final Duration duration) {
        return new Timer(duration);
    }

    /**
     * Checks if the timer is triggerd at the given {@link Time}. If the timer is
     * triggerd the next trigger {@link Time} is set. A second check will always
     * return false. ({@link Time} must be provided from outside to reduce cpu load.
     */
    public boolean isTick(final Time time) {
        final boolean isNow = time.isAfter(nextTick);
        if (isNow) {
            nextTick = time.plus(duration);
        }
        return isNow;
    }
}
