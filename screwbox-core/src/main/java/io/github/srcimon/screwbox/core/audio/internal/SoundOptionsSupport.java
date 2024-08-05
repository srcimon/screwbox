package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.AudioConfiguration;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.graphics.Camera;

import static io.github.srcimon.screwbox.core.utils.MathUtil.modifier;
import static java.util.Objects.isNull;

public class SoundOptionsSupport {

    private final Camera camera;
    private final AudioConfiguration configuration;

    public SoundOptionsSupport(final Camera camera, final AudioConfiguration configuration) {
        this.camera = camera;
        this.configuration = configuration;
    }
    public SoundOptions determinActualOptions(SoundOptions options) {
        SoundOptions in = calculateCurrent(options);
        return in.volume(calculateVolume(in));
    }

    private Percent calculateVolume(final SoundOptions options) {
        if (options.isMusic()) {
            final var musicVolume = configuration.isMusicMuted() ? Percent.zero() : configuration.musicVolume();
            return musicVolume.multiply(options.volume().value());
        }
        final var effectVolume = configuration.areEffectsMuted() ? Percent.zero() : configuration.effectVolume();
        return effectVolume.multiply(options.volume().value());
    }

    private SoundOptions calculateCurrent(SoundOptions options) {
        if (isNull(options.position())) {
            return options;
        }
        final var distance = camera.position().distanceTo(options.position());
        final var direction = modifier(options.position().x() - camera.position().x());
        final var quotient = distance / configuration.soundRange();
        return options
                .pan(direction * quotient)
                .volume(Percent.of(1 - quotient));
    }
}
