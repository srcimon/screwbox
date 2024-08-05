package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
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
        final Vector position = options.position();
        Percent in = isNull(position)
                ? options.volume()
                : Percent.of(calculateQuotient(position)).invert();

        if (options.isMusic()) {
            final var musicVolume = configuration.isMusicMuted() ? Percent.zero() : configuration.musicVolume();
            return musicVolume.multiply(in.value());
        }
        final var effectVolume = configuration.areEffectsMuted() ? Percent.zero() : configuration.effectVolume();
        return effectVolume.multiply(in.value());
    }

    public double currentPan(final SoundOptions options) {
        final Vector position = options.position();
        return isNull(position)
                ? options.pan()
                : calculateDirection(options) * calculateQuotient(position);
    }

    private double calculateDirection(SoundOptions options) {
        return modifier(options.position().x() - camera.position().x());
    }

    private double calculateQuotient(Vector position) {
        final var distance = camera.position().distanceTo(position);
        return distance / configuration.soundRange();
    }
}
