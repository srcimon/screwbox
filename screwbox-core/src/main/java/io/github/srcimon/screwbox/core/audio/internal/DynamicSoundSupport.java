package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.AudioConfiguration;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.graphics.Camera;

import static io.github.srcimon.screwbox.core.utils.MathUtil.modifier;
import static java.util.Objects.isNull;

public class DynamicSoundSupport {

    private final Camera camera;
    private final AudioConfiguration configuration;

    public DynamicSoundSupport(final Camera camera, final AudioConfiguration configuration) {
        this.camera = camera;
        this.configuration = configuration;
    }

    public Percent currentVolume(SoundOptions options) {
        Percent in = calculateCurrent(options);
        return calculateVolume(in, options.isMusic());
    }

    public double currentPan(final SoundOptions options) {
        return isNull(options.position())
                ? options.pan()
                : calculateDirection(options) * calculateQuotient(options);
    }

    private Percent calculateVolume(final Percent in, boolean isMusic) {
        if (isMusic) {
            final var musicVolume = configuration.isMusicMuted() ? Percent.zero() : configuration.musicVolume();
            return musicVolume.multiply(in.value());
        }
        final var effectVolume = configuration.areEffectsMuted() ? Percent.zero() : configuration.effectVolume();
        return effectVolume.multiply(in.value());
    }

    private Percent calculateCurrent(SoundOptions options) {
        if (isNull(options.position())) {
            return options.volume();
        }
        return Percent.of(calculateQuotient(options)).invert();
    }

    private double calculateDirection(SoundOptions options) {
        return modifier(options.position().x() - camera.position().x());
    }

    private double calculateQuotient(SoundOptions options) {
        final var distance = camera.position().distanceTo(options.position());
        return distance / configuration.soundRange();
    }
}
