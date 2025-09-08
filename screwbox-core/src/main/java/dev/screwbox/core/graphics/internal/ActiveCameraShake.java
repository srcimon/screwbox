package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Angle;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.options.CameraShakeOptions;
import dev.screwbox.core.utils.Noise;

import static dev.screwbox.core.Vector.$;

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

    Angle calculateSwing(final Time now) {
        return Angle.degrees(options.swing().degrees() * shakeNoise.value(now) * strengthAtTime(now));
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
