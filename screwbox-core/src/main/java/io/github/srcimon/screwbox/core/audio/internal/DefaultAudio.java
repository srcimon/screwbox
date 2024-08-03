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
import io.github.srcimon.screwbox.core.audio.internal.SoundManagement.ManagedSound;
import io.github.srcimon.screwbox.core.graphics.Camera;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static io.github.srcimon.screwbox.core.audio.AudioConfigurationEvent.ConfigurationProperty.EFFECTS_VOLUME;
import static io.github.srcimon.screwbox.core.audio.AudioConfigurationEvent.ConfigurationProperty.MUSIC_VOLUME;
import static io.github.srcimon.screwbox.core.utils.MathUtil.modifier;
import static java.util.Objects.requireNonNull;

public class DefaultAudio implements Audio, AudioConfigurationListener {

    private final SoundManagement soundManagement = new SoundManagement();

    private final ExecutorService executor;
    private final AudioAdapter audioAdapter;
    private final Camera camera;
    private final AudioConfiguration configuration = new AudioConfiguration().addListener(this);
    private final VolumeMonitor volumeMonitor;
    private final DataLinePool dataLinePool;

    public DefaultAudio(final ExecutorService executor, final AudioAdapter audioAdapter, final Camera camera, final DataLinePool dataLinePool) {
        this.executor = executor;
        this.audioAdapter = audioAdapter;
        this.camera = camera;
        this.volumeMonitor = new VolumeMonitor(executor, audioAdapter, configuration);
        this.dataLinePool = dataLinePool;
        this.executor.execute(() -> {
            dataLinePool.startNewLine(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false));
            dataLinePool.startNewLine(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000, 16, 2, 4, 48000, false));
        });
    }

    @Override
    public Audio stopAllSounds() {
        for (final var managedSound : soundManagement.activeSounds()) {
            managedSound.stop();
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
            managedSound.stop();
        }
        return this;
    }

    private void playSound(final Playback playback) {
        final Percent volume = (playback.options().isMusic() ? musicVolume() : effectVolume()).multiply(playback.options().volume().value());
        //TODO playback actualVolume();
        if (!volume.isZero()) {

            executor.execute(() -> {
                int loop = 0;
                ManagedSound managedSound = soundManagement.add(playback);
                while (loop < playback.options().times() && !managedSound.isShutdown()) {
                    loop++;
                    try (var stream = AudioAdapter.getAudioInputStream(playback.sound().content())) {
                        var line = dataLinePool.getLine(stream.getFormat());
                        managedSound.setLine(line);
                        audioAdapter.setVolume(line, volume);
                        audioAdapter.setBalance(line, playback.options().balance());
                        audioAdapter.setPan(line, playback.options().pan());
                        final byte[] bufferBytes = new byte[4096];
                        int readBytes;
                        while ((readBytes = stream.read(bufferBytes)) != -1 && !managedSound.isShutdown()) {
                            line.write(bufferBytes, 0, readBytes);
                        }
                        dataLinePool.freeLine(line);
                        soundManagement.remove(managedSound);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
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
        if (MUSIC_VOLUME.equals(event.changedProperty())) {
            for (final var playback : soundManagement.activeSounds()) {
                if (!playback.isEffect()) {
                    audioAdapter.setVolume(playback.line(), musicVolume().multiply(playback.volume().value()));
                }
            }

        } else if (EFFECTS_VOLUME.equals(event.changedProperty())) {
            for (final var playback : soundManagement.activeSounds()) {
                if (playback.isEffect()) {
                    audioAdapter.setVolume(playback.line(), effectVolume().multiply(playback.volume().value()));
                }
            }
        }
    }

    private Percent musicVolume() {
        return configuration.isMusicMuted() ? Percent.zero() : configuration.musicVolume();
    }

    private Percent effectVolume() {
        return configuration.areEffectsMuted() ? Percent.zero() : configuration.effectVolume();
    }
}