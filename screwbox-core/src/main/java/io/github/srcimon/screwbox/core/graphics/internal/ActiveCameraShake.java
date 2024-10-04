package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.CameraShakeOptions;
import io.github.srcimon.screwbox.core.utils.Noise;

import static io.github.srcimon.screwbox.core.Vector.$;

class ActiveCameraShake {

    private final Noise xNoise;
    private final Noise yNoise;
    private final Noise shakeNoise;
    private final Time start = Time.now();
    private final Duration duration;
    private final double xStrength;
    private final double yStrength;
    private final Rotation screenShake;
    public ActiveCameraShake(final CameraShakeOptions options) {
        xNoise = Noise.variableInterval(options.interval());
        yNoise = Noise.variableInterval(options.interval());
        shakeNoise = Noise.variableInterval(options.interval());
        duration = options.duration();
        xStrength = options.xStrength();
        yStrength = options.yStrength();
        screenShake = options.screenShake();//TODO reduce variables?
    }

    Vector calculateDistortion(final Time now, final double zoom) {
        final var progress = calculateProgress(now);

        return $(xNoise.value(now) * xStrength * progress.invert().value() / zoom,
                yNoise.value(now) * yStrength * progress.invert().value() / zoom);
    }

    boolean hasEnded(final Time now) {
        return !duration.isNone() && now.isAfter(duration.addTo(start));
    }

    private Percent calculateProgress(final Time now) {
        if (duration.isNone()) {
            return Percent.zero();
        }
        final Duration elapsed = Duration.between(start, now);
        final var end = duration.addTo(start);
        return Percent.of(1.0 * elapsed.nanos() / Duration.between(start, end).nanos());
    }

    //TODO test
    public Rotation caclulateRotation(final Time now) {
        final var progress = calculateProgress(now);
        return Rotation.degrees(screenShake.degrees() * shakeNoise.value(now) * progress.invert().value());
    }
}
