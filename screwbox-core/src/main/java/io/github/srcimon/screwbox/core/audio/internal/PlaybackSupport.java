package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.AudioConfiguration;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.graphics.Camera;

import static io.github.srcimon.screwbox.core.utils.MathUtil.modifier;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class PlaybackSupport {

    private final Camera camera;
    private final AudioConfiguration configuration;
    private final AudioAdapter audioAdapter;

    public PlaybackSupport(final AudioAdapter audioAdapter, final Camera camera, final AudioConfiguration configuration) {
        this.camera = camera;
        this.configuration = configuration;
        this.audioAdapter = audioAdapter;
    }

    public void applyOptionsOnPlayback(final DefaultAudio.ActivePlayback playback) {
        if (nonNull(playback.line())) {
            var actual = determinActualOptions(playback.options());
            audioAdapter.setVolume(playback.line(), actual.volume());
            audioAdapter.setPan(playback.line(), actual.pan());
        }
    }

    private SoundOptions determinActualOptions(SoundOptions options) {
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
