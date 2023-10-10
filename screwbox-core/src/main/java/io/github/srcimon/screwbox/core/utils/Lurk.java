package io.github.srcimon.screwbox.core.utils;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;

public class Lurk {

    private final Duration interval;

    public static Lurk fixedInterval(final Duration interval) {
        return new Lurk(interval);
    }

    private Lurk(final Duration interval) {
        this.interval = interval;
    }

    public double value(final Time time) {
        return 0;
    }
}
