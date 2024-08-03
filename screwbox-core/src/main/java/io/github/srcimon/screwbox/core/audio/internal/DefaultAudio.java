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
    private final AudioConfiguration configuration = new AudioConfiguration().addListener(this);
    private final VolumeMonitor volumeMonitor;
    private final SoundManagement soundManagement;

    public DefaultAudio(final ExecutorService executor, final AudioAdapter audioAdapter, final Camera camera, SoundManagement soundManagement) {
        this.executor = executor;
        this.camera = camera;
        this.volumeMonitor = new VolumeMonitor(executor, audioAdapter, configuration);
        this.soundManagement = soundManagement;
        //TODO Implement somewhere else
//        this.executor.execute(() -> {
//            dataLinePool.startNewLine(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false));
//            dataLinePool.startNewLine(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000, 16, 2, 4, 48000, false));
//        });
    }

    @Override
    public Audio stopAllSounds() {
        for (final var managedSound : soundManagement.activeSounds()) {
            soundManagement.stop(managedSound);
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
    public List<Playback> activePlaybacks() {
        List<Playback> activePlaybacks = new ArrayList<>();
        for (final var managedSound : soundManagement.activeSounds()) {
            activePlaybacks.add(managedSound.playback());
        }
        return activePlaybacks;
    }

    @Override
    public Audio playSound(final Sound sound, final SoundOptions options) {
        requireNonNull(sound, "sound must not be null");
        requireNonNull(options, "options must not be null");
        playSound(new Playback(sound, options, null));
        return this;
    }

    @Override
    public Audio stopSound(final Sound sound) {
        requireNonNull(sound, "sound must not be null");
        for (final var managedSound : soundManagement.fetchActiveSounds(sound)) {
            soundManagement.stop(managedSound);
        }
        return this;
    }

    @Override
    public int activeCount(final Sound sound) {
        return soundManagement.fetchActiveSounds(sound).size();
    }

    @Override
    public boolean isActive(final Sound sound) {
        return activeCount(sound) > 0;
    }

    @Override
    public int activeCount() {
        return soundManagement.activeSounds().size();
    }

    @Override
    public AudioConfiguration configuration() {
        return configuration;
    }

    @Override
    public void configurationChanged(final AudioConfigurationEvent event) {
        if (MUSIC_VOLUME.equals(event.changedProperty()) || EFFECTS_VOLUME.equals(event.changedProperty())) {
            for (final var managedSound : soundManagement.activeSounds()) {
                Percent volume = calculateVolume(managedSound.playback());
                soundManagement.changeVolume(managedSound, volume);
            }
        }
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
            executor.execute(() -> soundManagement.play(playback, volume));
        }
    }
}