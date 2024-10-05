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
    private final CameraShakeOptions options;

    public ActiveCameraShake(final CameraShakeOptions options) {
        xNoise = Noise.variableInterval(options.interval());
        yNoise = Noise.variableInterval(options.interval());
        shakeNoise = Noise.variableInterval(options.interval());
        this.options = options;
    }

    Vector calculateDistortion(final Time now, final double zoom) {
        final var progress = calculateProgress(now);

        return $(xNoise.value(now) * options.xStrength() * progress.invert().value() / zoom,
                yNoise.value(now) * options.yStrength() * progress.invert().value() / zoom);
    }

    boolean hasEnded(final Time now) {
        return !options.duration().isNone() && now.isAfter(options.duration().addTo(start));
    }

    private Percent calculateProgress(final Time now) {
        if (options.duration().isNone()) {
            return Percent.zero();
        }
        final Duration elapsed = Duration.between(start, now);
        final var end = options.duration().addTo(start);
        return Percent.of(1.0 * elapsed.nanos() / Duration.between(start, end).nanos());
    }

    //TODO test
    public Rotation caclulateRotation(final Time now) {
        final var progress = calculateProgress(now);
        return Rotation.degrees(options.shake().degrees() * shakeNoise.value(now) * progress.invert().value());
    }
}
