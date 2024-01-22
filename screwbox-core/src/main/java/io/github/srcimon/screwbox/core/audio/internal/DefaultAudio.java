package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.Audio;
import io.github.srcimon.screwbox.core.audio.Sound;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class DefaultAudio implements Audio, LineListener {

    private final ExecutorService executor;
    private final AudioAdapter audioAdapter;
    private final Map<Clip, ActiveSound> activeSounds = new ConcurrentHashMap<>();
    private Percent effectVolume = Percent.max();
    private Percent musicVolume = Percent.max();

    public DefaultAudio(final ExecutorService executor, final AudioAdapter audioAdapter) {
        this.executor = executor;
        this.audioAdapter = audioAdapter;
    }

    @Override
    public Audio playMusic(final Sound sound) {
        playClip(new ActiveSound(sound, true), musicVolume, true);
        return this;
    }

    @Override
    public Audio playMusicLooped(Sound sound) {
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
        this.effectVolume = volume;
        updateVolumeOfActiveClips(volume, false);
        return this;
    }

    @Override
    public Audio setMusicVolume(final Percent volume) {
        this.musicVolume = volume;
        updateVolumeOfActiveClips(volume, true);
        return this;
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
                for (final Clip clip : activeClips()) {
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

    private void playClip(final ActiveSound activeSound, final Percent volume, final boolean looped) {
        if (!volume.isZero()) {
            executor.execute(() -> {
                final Clip clip = audioAdapter.createClip(activeSound.sound(), volume);
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
    public Percent effectVolume() {
        return effectVolume;
    }

    @Override
    public Percent musicVolume() {
        return musicVolume;
    }

    @Override
    public Audio muteMusic() {
        setMusicVolume(Percent.zero());
        return this;
    }

    @Override
    public Audio muteEffects() {
        setEffectVolume(Percent.zero());
        return this;
    }

    @Override
    public Audio mute() {
        muteEffects();
        muteMusic();
        return this;
    }

    private List<Clip> activeClips() {
        return new ArrayList<>(activeSounds.keySet());
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
