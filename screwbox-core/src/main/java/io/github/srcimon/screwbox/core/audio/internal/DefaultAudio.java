package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.*;
import io.github.srcimon.screwbox.core.utils.Cache;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import static io.github.srcimon.screwbox.core.audio.AudioConfigurationEvent.ConfigurationProperty.EFFECTS_VOLUME;
import static io.github.srcimon.screwbox.core.audio.AudioConfigurationEvent.ConfigurationProperty.MUSIC_VOLUME;

public class DefaultAudio implements Audio, LineListener, AudioConfigurationListener {

    private static final Cache<Sound, Clip> CLIP_CACHE = new Cache<>();

    private final ExecutorService executor;
    private final AudioAdapter audioAdapter;
    private final Map<Clip, ActiveSound> activeSounds = new ConcurrentHashMap<>();

    private final AudioConfiguration configuration = new AudioConfiguration();

    public DefaultAudio(final ExecutorService executor, final AudioAdapter audioAdapter) {
        this.executor = executor;
        this.audioAdapter = audioAdapter;
        configuration.addListener(this);
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
                final List<Clip> activeClips = new ArrayList<>(activeSounds.keySet());
                for (final Clip clip : activeClips) {
                    clip.stop();
                }
                activeSounds.clear();
            });
        }
        return this;
    }

    @Override
    public Audio playEffect(final Sound sound, final SoundOptions options) {
        playClip(new ActiveSound(sound, false), options);
        return this;
    }

    @Override
    public Audio playMusic(final Sound sound, final SoundOptions options) {
        playClip(new ActiveSound(sound, true), options);
        return this;
    }

    @Override
    public Audio stop(final Sound sound) {
        for (final Clip clip : fetchClipsFor(sound)) {
            executor.execute(clip::stop);
        }
        return this;
    }

    @Override
    public void update(final LineEvent event) {
        if (event.getType().equals(LineEvent.Type.STOP)) {
            activeSounds.remove(event.getSource());
        }
    }

    private void playClip(final ActiveSound activeSound, final SoundOptions options) {
        final Percent volume = activeSound.isMusic() ? musicVolume() : effectVolume();
        if (!volume.isZero()) {
            executor.execute(() -> {
                final Sound sound = activeSound.sound();
                final Clip clip = isActive(sound)
                        ? audioAdapter.createClip(sound)
                        : CLIP_CACHE.getOrElse(sound, () -> audioAdapter.createClip(sound));
                audioAdapter.setVolume(clip, volume);
                activeSounds.put(clip, activeSound);
                clip.setFramePosition(0);
                clip.addLineListener(this);
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
        for (final var activeSound : activeSounds.entrySet()) {
            if (activeSound.getValue().sound().equals(sound)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int activeCount() {
        return activeSounds.size();
    }

    @Override
    public AudioConfiguration configuration() {
        return configuration;
    }

    @Override
    public void configurationChanged(final AudioConfigurationEvent event) {
        if (MUSIC_VOLUME.equals(event.changedProperty())) {
            for (final var activeSound : activeSounds.entrySet()) {
                if (activeSound.getValue().isMusic()) {
                    audioAdapter.setVolume(activeSound.getKey(), musicVolume());
                }
            }
        } else if (EFFECTS_VOLUME.equals(event.changedProperty())) {
            for (final var activeSound : activeSounds.entrySet()) {
                if (activeSound.getValue().isEffect()) {
                    audioAdapter.setVolume(activeSound.getKey(), effectVolume());
                }
            }
        }
    }

    private List<Clip> fetchClipsFor(final Sound sound) {
        final List<Clip> clips = new ArrayList<>();
        for (final var activeSound : activeSounds.entrySet()) {
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