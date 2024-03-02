package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.utils.Lurk;

import static io.github.srcimon.screwbox.core.Vector.$;

public class CameraShake {

    private final double strength;
    private Duration interval;
    private Duration duration = Duration.none();

    private Lurk x = Lurk.intervalWithDeviation(Duration.ofMillis(200), Percent.half());
    private Lurk y = Lurk.intervalWithDeviation(Duration.ofMillis(200), Percent.half());

    private CameraShake(final double strength) {
        this.strength = strength;
    }
    public static CameraShake strength(final double strength) {
        //TODO validate > 0
        return new CameraShake(strength);
    }

    public CameraShake duration(final Duration duration) {
        this.duration = duration;
        return this;
    }

    public CameraShake interval(final Duration interval) {
        this.interval = interval;
        return this;
    }

    public Vector getDistorion(Time start, Time now) {
        Duration elapsed = Duration.between(start, now);
        Time end = now.plus(duration);
        var progress = Percent.of(1.0 * elapsed.nanos() / Duration.between(start, end).nanos());
        return $(x.value(now), y.value(now)).multiply(strength * progress.invert().value());
    }

    public boolean isFinished(Time start, Time now) {
        if(duration.nanos() == 0) {//TODO: isNone
            return false;
        }
        return now.isAfter(start.plus(duration));
    }
}
