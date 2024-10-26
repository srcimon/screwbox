package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.audio.AudioConfiguration;
import io.github.srcimon.screwbox.core.audio.SoundOptions;

import static java.util.Objects.isNull;

public class DynamicSoundSupport {

    private final AudioConfiguration configuration;
    private final VisualAttention visualAttention;

    public DynamicSoundSupport(final VisualAttention visualAttention, final AudioConfiguration configuration) {
        this.visualAttention = visualAttention;
        this.configuration = configuration;
    }

    public Percent currentVolume(final SoundOptions options) {
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
        return visualAttention.xDirection(position) * distanceModifier(position);
    }

    private double distanceModifier(final Vector position) {
        return visualAttention.distanceTo(position) / configuration.soundRange();
    }

    private Percent effectVolume() {
        return configuration.areEffectsMuted() ? Percent.zero() : configuration.effectVolume();
    }

    private Percent musicVolume() {
        return configuration.isMusicMuted() ? Percent.zero() : configuration.musicVolume();
    }
}
