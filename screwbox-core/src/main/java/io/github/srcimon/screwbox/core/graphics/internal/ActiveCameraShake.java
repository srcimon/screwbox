package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.CameraShakeOptions;
import io.github.srcimon.screwbox.core.utils.Noise;

import static io.github.srcimon.screwbox.core.Vector.$;

class ActiveCameraShake {

    private final Noise noiseX;
    private final Noise noiseY;
    private final Time start = Time.now();
    private final Duration duration;
    private final double xStrength;
    private final double yStrength;

    public ActiveCameraShake(final CameraShakeOptions options) {
        noiseX = Noise.variableInterval(options.interval());
        noiseY = Noise.variableInterval(options.interval());
        duration = options.duration();
        xStrength = options.xStrength();
        yStrength = options.yStrength();
    }

    Vector calculateDistortion(final Time now, final double zoom) {
        final var progress = calculateProgress(now);

        return $(noiseX.value(now) * xStrength * progress.invert().value() / zoom,
                noiseY.value(now) * yStrength * progress.invert().value() / zoom);
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
}
