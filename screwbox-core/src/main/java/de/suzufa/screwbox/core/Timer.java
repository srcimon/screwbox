package de.suzufa.screwbox.core;

public class Timer {

    private final Duration duration;
    private Time nextTick;

    private Timer(final Duration duration) {
        this.duration = duration;
        this.nextTick = Time.now().plus(duration);

    }

    public static Timer withIntervalOf(final Duration duration) {
        return new Timer(duration);
    }

    public boolean isNow(final Time time) {
        final boolean isNow = time.isAfter(nextTick);
        if (isNow) {
            nextTick = time.plus(duration);
        }
        return isNow;
    }
}
