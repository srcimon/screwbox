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
        final Percent directionalVolume = isNull(position)
                ? options.volume()
                : Percent.of(distanceModifier(position)).invert();

        final Percent configuredVolume = options.isMusic() ? musicVolume() : effectVolume();
        return directionalVolume.multiply(configuredVolume.value());
    }

    public double currentPan(final SoundOptions options) {
        final Vector position = options.position();
        return isNull(position)
                ? options.pan()
                : panByRelativePosition(position);
    }

    private double panByRelativePosition(final Vector position) {
        return modifier(position.x() - camera.position().x()) * distanceModifier(position);
    }

    private double distanceModifier(final Vector position) {
        return camera.position().distanceTo(position) / configuration.soundRange();
    }

    private Percent effectVolume() {
        return configuration.areEffectsMuted() ? Percent.zero() : configuration.effectVolume();
    }

    private Percent musicVolume() {
        return configuration.isMusicMuted() ? Percent.zero() : configuration.musicVolume();
    }
}
