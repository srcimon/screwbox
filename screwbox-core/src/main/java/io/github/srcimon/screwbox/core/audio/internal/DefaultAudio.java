package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.audio.*;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.utils.Cache;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import static io.github.srcimon.screwbox.core.audio.AudioConfigurationEvent.ConfigurationProperty.EFFECTS_VOLUME;
import static io.github.srcimon.screwbox.core.audio.AudioConfigurationEvent.ConfigurationProperty.MUSIC_VOLUME;
import static io.github.srcimon.screwbox.core.utils.MathUtil.modifier;

public class DefaultAudio implements Audio, AudioConfigurationListener {

    private static final Cache<Sound, Clip> CLIP_CACHE = new Cache<>();

    private final ExecutorService executor;
    private final AudioAdapter audioAdapter;
    private final Graphics graphics;
    private final Map<Clip, Playback> playbacks = new ConcurrentHashMap<>();
    private final AudioConfiguration configuration = new AudioConfiguration().addListener(this);

    public DefaultAudio(final ExecutorService executor, final AudioAdapter audioAdapter, final Graphics graphics) {
        this.executor = executor;
        this.audioAdapter = audioAdapter;
        this.graphics = graphics;
    }

    public void shutdown() {
        synchronized (this) {
            executor.shutdown();
        }
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

    //TODO Test
    @Override
    public Audio playSound(final Sound sound, final Vector position) {
        final var distance = graphics.cameraPosition().distanceTo(position);
        final var direction = modifier(position.x() - graphics.cameraPosition().x());
        final var quotient = distance / configuration.soundDistance();
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
    public Audio stop(final Sound sound) {
        for (final Clip clip : fetchClipsFor(sound)) {
            executor.execute(clip::stop);
        }
        return this;
    }

    private void playSound(final Sound sound, final SoundOptions options, final Vector position) {
        final Percent configVolume = options.isMusic() ? musicVolume() : effectVolume();
        final Percent volume = configVolume.multiply(options.volume().value());
        if (!volume.isZero()) {
            executor.execute(() -> {
                final Clip clip = isActive(sound)
                        ? audioAdapter.createClip(sound)
                        : CLIP_CACHE.getOrElse(sound, () -> audioAdapter.createClip(sound));
                audioAdapter.setVolume(clip, volume);
                audioAdapter.setBalance(clip, options.balance());
                audioAdapter.setPan(clip, options.pan());
                playbacks.put(clip, new Playback(sound, options, options.isMusic(), position));
                clip.setFramePosition(0);
                clip.addLineListener(event -> {
                    if (event.getType().equals(LineEvent.Type.STOP)) {
                        playbacks.remove(event.getSource());
                    }
                });
                clip.loop(options.times() - 1);
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
                if (activeSound.getValue().isMusic()) {
                    audioAdapter.setVolume(activeSound.getKey(), musicVolume().multiply(activeSound.getValue().options().volume().value()));
                }
            }
        } else if (EFFECTS_VOLUME.equals(event.changedProperty())) {
            for (final var activeSound : playbacks.entrySet()) {
                if (activeSound.getValue().isEffect()) {
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