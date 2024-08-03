package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.audio.Audio;
import io.github.srcimon.screwbox.core.audio.AudioConfiguration;
import io.github.srcimon.screwbox.core.audio.AudioConfigurationEvent;
import io.github.srcimon.screwbox.core.audio.AudioConfigurationListener;
import io.github.srcimon.screwbox.core.audio.Playback;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.audio.SoundOptions;
import io.github.srcimon.screwbox.core.graphics.Camera;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static io.github.srcimon.screwbox.core.audio.AudioConfigurationEvent.ConfigurationProperty.EFFECTS_VOLUME;
import static io.github.srcimon.screwbox.core.audio.AudioConfigurationEvent.ConfigurationProperty.MUSIC_VOLUME;
import static io.github.srcimon.screwbox.core.utils.MathUtil.modifier;
import static java.util.Objects.requireNonNull;

public class DefaultAudio implements Audio, AudioConfigurationListener {

    private final ExecutorService executor;
    private final Camera camera;
    private final AudioConfiguration configuration;
    private final VolumeMonitor volumeMonitor;
    private final PlaybackTracker playbackTracker;

    public DefaultAudio(final ExecutorService executor, final AudioConfiguration configuration,
                        final VolumeMonitor volumeMonitor, final Camera camera, final PlaybackTracker playbackTracker) {
        this.executor = executor;
        this.camera = camera;
        this.volumeMonitor = volumeMonitor;
        this.playbackTracker = playbackTracker;
        this.configuration = configuration;
        configuration.addListener(this);
    }

    @Override
    public Audio stopAllSounds() {
        for (final var managedSound : playbackTracker.allActive()) {
            playbackTracker.stop(managedSound);
        }
        return this;
    }

    @Override
    public Percent microphoneLevel() {
        return volumeMonitor.level();
    }

    @Override
    public boolean isMicrophoneActive() {
        return volumeMonitor.isActive();
    }

    @Override
    public Audio playSound(final Sound sound, final Vector position) {
        requireNonNull(sound, "sound must not be null");
        requireNonNull(position, "position must not be null");
        final var distance = camera.position().distanceTo(position);
        final var direction = modifier(position.x() - camera.position().x());
        final var quotient = distance / configuration.soundRange();
        final var options = SoundOptions.playOnce()
                .pan(direction * quotient)
                .volume(Percent.of(1 - quotient));
        playSound(new Playback(sound, options, position));
        return this;
    }

    @Override
    public Audio playSound(final Sound sound, final SoundOptions options) {
        requireNonNull(sound, "sound must not be null");
        requireNonNull(options, "options must not be null");
        playSound(new Playback(sound, options, null));
        return this;
    }

//    @Override
//    public List<Playback> activePlaybacks() {
//        List<Playback> activePlaybacks = new ArrayList<>();
//        for (final var activePlayback : playbackTracker.allActive()) {
//            activePlaybacks.add(activePlayback.playback());
//        }
//        return activePlaybacks;
//    }

    @Override
    public Audio stopSound(final Sound sound) {
        requireNonNull(sound, "sound must not be null");
        for (final var activePlayback : fetchPlaybacks(sound)) {
            playbackTracker.stop(activePlayback);
        }
        return this;
    }

    @Override
    public int activeCount(final Sound sound) {
        return fetchPlaybacks(sound).size();
    }

    @Override
    public boolean isActive(final Sound sound) {
        return activeCount(sound) > 0;
    }

    @Override
    public int activeCount() {
        return playbackTracker.allActive().size();
    }

    @Override
    public AudioConfiguration configuration() {
        return configuration;
    }

    @Override
    public void configurationChanged(final AudioConfigurationEvent event) {
        if (MUSIC_VOLUME.equals(event.changedProperty()) || EFFECTS_VOLUME.equals(event.changedProperty())) {
            for (final var managedSound : playbackTracker.allActive()) {
                Percent volume = calculateVolume(managedSound.playback());
                playbackTracker.changeVolume(managedSound, volume);
            }
        }
    }

    private List<PlaybackTracker.ActivePlayback> fetchPlaybacks(final Sound sound) {
        final var active = new ArrayList<PlaybackTracker.ActivePlayback>();
        for (final var activeSound : playbackTracker.allActive()) {
            if (activeSound.playback().sound().equals(sound)) {
                active.add(activeSound);
            }
        }
        return active;
    }

    private Percent calculateVolume(final Playback playback) {
        if (playback.options().isMusic()) {
            final var musicVolume = configuration.isMusicMuted() ? Percent.zero() : configuration.musicVolume();
            return musicVolume.multiply(playback.options().volume().value());
        }
        final var effectVolume = configuration.areEffectsMuted() ? Percent.zero() : configuration.effectVolume();
        return effectVolume.multiply(playback.options().volume().value());
    }

    private void playSound(final Playback playback) {
        final Percent volume = calculateVolume(playback);
        if (!volume.isZero()) {
            executor.execute(() -> playbackTracker.play(playback, volume));
        }
    }
}