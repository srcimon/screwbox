package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CameraShakeOptions;
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

    Rotation caclulateRotation(final Time now) {
        return Rotation.degrees(options.screenRotation().degrees() * shakeNoise.value(now) * strengthAtTime(now));
    }

    Vector calculateDistortion(final Time now, final double zoom) {
        return $(xNoise.value(now) * options.xStrength() * strengthAtTime(now) / zoom,
                yNoise.value(now) * options.yStrength() * strengthAtTime(now) / zoom);
    }

    boolean hasEnded(final Time now) {
        return !options.duration().isNone() && now.isAfter(options.duration().addTo(start));
    }

    private double strengthAtTime(final Time now) {
        final var progress = options.duration().isNone()
                ? Percent.zero()
                : options.duration().progress(start, now);

        return options.ease().applyOn(progress).value();
    }
}
