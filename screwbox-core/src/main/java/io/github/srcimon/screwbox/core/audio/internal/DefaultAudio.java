package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.Audio;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.utils.Cache;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class DefaultAudio implements Audio, LineListener {

    private static final Cache<Sound, Clip> CLIP_CACHE =new Cache<>();

    private final ExecutorService executor;
    private final AudioAdapter audioAdapter;
    private final Map<Clip, ActiveSound> activeSounds = new ConcurrentHashMap<>();
    private Volume effectVolume = new Volume();
    private Volume musicVolume = new Volume();

    public DefaultAudio(final ExecutorService executor, final AudioAdapter audioAdapter) {
        this.executor = executor;
        this.audioAdapter = audioAdapter;
    }

    @Override
    public Audio playMusic(final Sound sound) {
        playClip(new ActiveSound(sound, true), musicVolume, false);
        return this;
    }

    @Override
    public Audio playMusicLooped(final Sound sound) {
        playClip(new ActiveSound(sound, true), musicVolume, true);
        return this;
    }

    @Override
    public Audio playEffect(final Sound sound) {
        playClip(new ActiveSound(sound, false), effectVolume, false);
        return this;
    }

    @Override
    public Audio playEffectLooped(final Sound sound) {
        playClip(new ActiveSound(sound, false), effectVolume, true);
        return this;
    }

    @Override
    public Audio setEffectVolume(final Percent volume) {
        effectVolume = effectVolume.updatedValue(volume);
        updateVolumeOfActiveClips(volume, false);
        return this;
    }

    @Override
    public Audio setMusicVolume(final Percent volume) {
        musicVolume = musicVolume.updatedValue(volume);
        updateVolumeOfActiveClips(volume, true);
        return this;
    }

    @Override
    public Percent effectVolume() {
        return effectVolume.value();
    }

    @Override
    public Percent musicVolume() {
        return musicVolume.value();
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

    private void playClip(final ActiveSound activeSound, final Volume volume, final boolean looped) {
        if (!volume.playbackVolume().isZero()) {
            executor.execute(() -> {
                final Sound sound = activeSound.sound();
                final Clip clip = isActive(sound)
                        ? audioAdapter.createClip(sound, volume.playbackVolume())
                        : CLIP_CACHE.getOrElse(sound, () -> audioAdapter.createClip(sound, volume.playbackVolume()));
                activeSounds.put(clip, activeSound);
                clip.setFramePosition(0);
                clip.addLineListener(this);
                start(clip, looped);
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
    public Audio setMusicMuted(boolean isMuted) {
        musicVolume = musicVolume.muted(isMuted);
        return this;
    }

    @Override
    public boolean isMusicMuted() {
        return musicVolume.isMuted();
    }

    @Override
    public Audio setEffectsMuted(boolean isMuted) {
        effectVolume = effectVolume.muted(isMuted);
        return this;
    }

    @Override
    public boolean areEffectsMuted() {
        return effectVolume.isMuted();
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

    private void start(final Clip clip, final boolean looped) {
        if (looped) {
            clip.loop(Integer.MAX_VALUE);
        } else {
            clip.start();
        }
    }

    private void updateVolumeOfActiveClips(final Percent volume, final boolean isMusic) {
        for (final var activeSound : activeSounds.entrySet()) {
            if (isMusic == activeSound.getValue().isMusic()) {
                audioAdapter.setVolume(activeSound.getKey(), volume);
            }
        }
    }
}
