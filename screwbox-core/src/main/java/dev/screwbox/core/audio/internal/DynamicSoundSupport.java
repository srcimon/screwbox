package dev.screwbox.core.audio.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.audio.AudioConfiguration;
import dev.screwbox.core.audio.SoundOptions;
import dev.screwbox.core.graphics.internal.AttentionFocus;

import static dev.screwbox.core.utils.MathUtil.modifier;
import static java.util.Objects.isNull;

public class DynamicSoundSupport {

    private final AudioConfiguration configuration;
    private final AttentionFocus attentionFocus;

    public DynamicSoundSupport(final AttentionFocus attentionFocus, final AudioConfiguration configuration) {
        this.attentionFocus = attentionFocus;
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
        final Vector direction = attentionFocus.direction(position);
        return modifier(direction.x()) * distanceModifier(position);
    }

    private double distanceModifier(final Vector position) {
        return attentionFocus.distanceTo(position) / configuration.soundRange();
    }

    private Percent effectVolume() {
        return configuration.areEffectsMuted() ? Percent.zero() : configuration.effectVolume();
    }

    private Percent musicVolume() {
        return configuration.isMusicMuted() ? Percent.zero() : configuration.musicVolume();
    }
}
