package de.suzufa.screwbox.core.audio.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.audio.Audio;
import de.suzufa.screwbox.core.audio.Sound;

public class DefaultAudio implements Audio, LineListener {

    private final ExecutorService executor;
    private final AudioAdapter audioAdapter;
    private final Map<Clip, Sound> activeSounds = new HashMap<>();
    private Percentage effectVolume = Percentage.max();
    private Percentage musicVolume = Percentage.max();

    public DefaultAudio(final ExecutorService executor, final AudioAdapter audioAdapter) {
        this.executor = executor;
        this.audioAdapter = audioAdapter;
    }

    private List<Clip> reverseLookup(final Sound sound) {
        final List<Clip> clips = new ArrayList<>();
        for (final var activeSound : activeSounds.entrySet()) {
            if (activeSound.getValue().equals(sound)) {
                clips.add(activeSound.getKey());
            }
        }
        return clips;
    }

    @Override
    public Audio playMusic(final Sound sound) {
        if (!musicVolume.isMinValue()) {
            playClip(sound, musicVolume, true);
        }
        return this;
    }

    @Override
    public Audio playEffect(final Sound sound) {
        if (!effectVolume.isMinValue()) {
            playClip(sound, effectVolume, false);
        }
        return this;
    }

    @Override
    public Audio playEffectLooped(final Sound sound) {
        if (!effectVolume.isMinValue()) {
            playClip(sound, effectVolume, true);
        }
        return this;
    }

    @Override
    public Audio setEffectVolume(final Percentage volume) {
        this.effectVolume = volume;
        return this;
    }

    @Override
    public Audio setMusicVolume(final Percentage volume) {
        this.musicVolume = volume;
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
                final List<Clip> clipsToStop = new ArrayList<>(activeSounds.keySet());
                for (final Clip clip : clipsToStop) {
                    clip.stop();
                }
                activeSounds.clear();
            });
        }
        return this;
    }

    @Override
    public Audio stop(final Sound sound) {
        for (final Clip clip : reverseLookup(sound)) {
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

    private void playClip(final Sound sound, final Percentage volume, final boolean looped) {
        executor.execute(() -> {
            final Clip clip = audioAdapter.createClip(sound, volume);
            activeSounds.put(clip, sound);
            clip.setFramePosition(0);
            clip.addLineListener(this);
            start(clip, looped);
        });
    }

    private void start(final Clip clip, final boolean looped) {
        if (looped) {
            clip.loop(Integer.MAX_VALUE);
        } else {
            clip.start();
        }
    }

    @Override
    public int activeCount(final Sound sound) {
        return reverseLookup(sound).size();
    }

    @Override
    public int activeCount() {
        return activeSounds.size();
    }

    @Override
    public Percentage effectVolume() {
        return effectVolume;
    }

    @Override
    public Percentage musicVolume() {
        return musicVolume;
    }

    @Override
    public Audio muteMusic() {
        setMusicVolume(Percentage.min());
        return this;
    }

    @Override
    public Audio muteEffects() {
        setEffectVolume(Percentage.min());
        return this;
    }

}
