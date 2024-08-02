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

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import static io.github.srcimon.screwbox.core.audio.AudioConfigurationEvent.ConfigurationProperty.EFFECTS_VOLUME;
import static io.github.srcimon.screwbox.core.audio.AudioConfigurationEvent.ConfigurationProperty.MUSIC_VOLUME;
import static io.github.srcimon.screwbox.core.utils.MathUtil.modifier;
import static java.util.Objects.requireNonNull;

public class DefaultAudio implements Audio, AudioConfigurationListener {

    private final ExecutorService executor;
    private final AudioAdapter audioAdapter;
    private final Camera camera;
    private final Map<Clip, Playback> playbacks = new ConcurrentHashMap<>();
    private final AudioConfiguration configuration = new AudioConfiguration().addListener(this);
    private final VolumeMonitor volumeMonitor;
    private final DataLinePool dataLinePool = new DataLinePool();

    public DefaultAudio(final ExecutorService executor, final AudioAdapter audioAdapter, final Camera camera) {
        this.executor = executor;
        this.audioAdapter = audioAdapter;
        this.camera = camera;
        this.volumeMonitor = new VolumeMonitor(executor, audioAdapter, configuration);
    }

    @Override
    public Audio stopAllSounds() {
        if (!executor.isShutdown()) {
            executor.execute(() -> {
                final List<Clip> activeClips = new ArrayList<>(playbacks.keySet());
                for (final Clip clip : activeClips) {
                    clip.stop();
                }
                playbacks.clear();
            });
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
        requireNonNull(position, "position must not be null");
        final var distance = camera.position().distanceTo(position);
        final var direction = modifier(position.x() - camera.position().x());
        final var quotient = distance / configuration.soundRange();
        final var options = SoundOptions.playOnce()
                .pan(direction * quotient)
                .volume(Percent.of(1 - quotient));

        playSound(sound, options, position);
        return this;
    }

    @Override
    public List<Playback> activePlaybacks() {
        return new ArrayList<>(playbacks.values());
    }

    @Override
    public Audio playSound(final Sound sound, final SoundOptions options) {
        playSound(sound, options, null);
        return this;
    }

    @Override
    public Audio stopSound(final Sound sound) {
        for (final Clip clip : fetchClipsFor(sound)) {
            executor.execute(clip::stop);
        }
        return this;
    }

    private void playSound(final Sound sound, final SoundOptions options, final Vector position) {
        requireNonNull(sound, "sound must not be null");
        requireNonNull(options, "options must not be null");
        final Percent configVolume = options.isMusic() ? musicVolume() : effectVolume();
        final Percent volume = configVolume.multiply(options.volume().value());
        if (!volume.isZero()) {
            executor.execute(() -> {
                try (var stream = AudioAdapter.getAudioInputStream(sound.content())) {
                    var format = stream.getFormat();
                    var line = dataLinePool.getLine(format);
                    audioAdapter.setVolume(line, volume);
                    audioAdapter.setBalance(line, options.balance());
                    audioAdapter.setPan(line, options.pan());
                    byte[] bufferBytes = new byte[4096];
                    int readBytes = -1;
                    while ((readBytes = stream.read(bufferBytes)) != -1) {
                        line.write(bufferBytes, 0, readBytes);
                    }
                    dataLinePool.freeLine(line);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

//                final Clip clip = audioAdapter.createClip(sound);
//                playbacks.put(clip, new Playback(sound, options, position));
//                clip.setFramePosition(0);
//                clip.addLineListener(event -> {
//                    if (event.getType().equals(LineEvent.Type.STOP)) {
//                        playbacks.remove(event.getSource());
//                    }
//                });
//                clip.loop(options.times() - 1);
            });
        }
    }

    @Override
    public int activeCount(final Sound sound) {
        return fetchClipsFor(sound).size();
    }

    @Override
    public boolean isActive(final Sound sound) {
        for (final var activeSound : playbacks.entrySet()) {
            if (activeSound.getValue().sound().equals(sound)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int activeCount() {
        return playbacks.size();
    }

    @Override
    public AudioConfiguration configuration() {
        return configuration;
    }

    @Override
    public void configurationChanged(final AudioConfigurationEvent event) {
        if (MUSIC_VOLUME.equals(event.changedProperty())) {
            for (final var activeSound : playbacks.entrySet()) {
                if (activeSound.getValue().options().isMusic()) {
                    audioAdapter.setVolume(activeSound.getKey(), musicVolume().multiply(activeSound.getValue().options().volume().value()));
                }
            }
        } else if (EFFECTS_VOLUME.equals(event.changedProperty())) {
            for (final var activeSound : playbacks.entrySet()) {
                if (activeSound.getValue().options().isEffect()) {
                    audioAdapter.setVolume(activeSound.getKey(), effectVolume().multiply(activeSound.getValue().options().volume().value()));
                }
            }
        }
    }

    private List<Clip> fetchClipsFor(final Sound sound) {
        final List<Clip> clips = new ArrayList<>();
        for (final var activeSound : playbacks.entrySet()) {
            if (activeSound.getValue().sound().equals(sound)) {
                clips.add(activeSound.getKey());
            }
        }
        return clips;
    }

    private Percent musicVolume() {
        return configuration.isMusicMuted() ? Percent.zero() : configuration.musicVolume();
    }

    private Percent effectVolume() {
        return configuration.areEffectsMuted() ? Percent.zero() : configuration.effectVolume();
    }

}