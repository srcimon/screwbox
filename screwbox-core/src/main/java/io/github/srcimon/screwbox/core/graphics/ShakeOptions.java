package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.utils.Noise;

import static io.github.srcimon.screwbox.core.Vector.$;

public class ShakeOptions {

    private final double strength;
    private Duration duration = Duration.none();
    private Duration interval = Duration.none();

    private ShakeOptions(final double strength) {
        this.strength = strength;
    }

    public static ShakeOptions strength(final double strength) {
        //TODO validate > 0
        return new ShakeOptions(strength);
    }

    public ShakeOptions duration(final Duration duration) {
        this.duration = duration;
        return this;
    }

    public ShakeOptions interval(final Duration interval) {
        this.interval = interval;
        return this;
    }

    public double strength() {
        return strength;
    }

    public Duration duration() {
        return duration;
    }

}
