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

    private Noise xNoise = Noise.variableInterval(Duration.ofMillis(200));
    private Noise yNoise = Noise.variableInterval(Duration.ofMillis(200));

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
        this.xNoise = Noise.variableInterval(interval);
        this.yNoise = Noise.variableInterval(interval);
        return this;
    }

    public Vector calculateDistortion(Time start, Time now) {
        Duration elapsed = Duration.between(start, now);
        Time end = now.plus(duration);
        var progress = duration.isNone() ? Percent.zero() : Percent.of(1.0 * elapsed.nanos() / Duration.between(start, end).nanos());
        return $(xNoise.value(now), yNoise.value(now)).multiply(strength * progress.invert().value());
    }

    public boolean isFinished(Time start, Time now) {
        if(duration.isNone()) {//TODO: isNone
            return false;
        }
        return now.isAfter(start.plus(duration));
    }
}
