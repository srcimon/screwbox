package de.suzufa.screwbox.core.audio.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.audio.Audio;
import de.suzufa.screwbox.core.audio.Sound;

public class DefaultAudio implements Audio, LineListener {

    private final ExecutorService executor;
    private final Map<Clip, Sound> activeSounds = new HashMap<>();
    private Percentage effectVolume = Percentage.max();
    private Percentage musicVolume = Percentage.max();

    public DefaultAudio(final ExecutorService executor) {
        this.executor = executor;
    }

    private List<Clip> reverseLookup(Sound sound) {
        List<Clip> clips = new ArrayList<>();
        for (var activeSound : activeSounds.entrySet()) {
            if (activeSound.getValue().equals(sound)) {
                clips.add(activeSound.getKey());
            }
        }
        return clips;
    }

    @Override
    public Audio playMusic(final Sound sound) {
        playClip(sound, musicVolume, true);
        return this;
    }

    @Override
    public Audio playEffect(final Sound sound) {
        playClip(sound, effectVolume, false);
        return this;
    }

    @Override
    public Audio playEffectLooped(final Sound sound) {
        playClip(sound, effectVolume, true);
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
    public Audio resume(final Sound sound) {
        for (Clip clip : reverseLookup(sound)) {
            executor.execute(() -> start(clip, false));
        }
        return this;
    }

    @Override
    public Audio resumeLooped(final Sound sound) {
        for (Clip clip : reverseLookup(sound)) {
            executor.execute(() -> start(clip, true));
        }
        return this;
    }

    @Override
    public Audio stop(final Sound sound) {
        for (Clip clip : reverseLookup(sound)) {
            executor.execute(() -> clip.stop());
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
            Clip clip = getClip(sound.content());

            activeSounds.put(clip, sound);

            clip.setFramePosition(0);
            clip.addLineListener(this);
            final FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(volume.value()));

            start(clip, looped);

        });
    }

    private Clip getClip(final byte[] content) {
        try (InputStream inputStream = new ByteArrayInputStream(content)) {
            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream)) {
                final Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                return clip;
            }
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            throw new IllegalStateException("could not create sound", e);
        }
    }

    private void start(final Clip clip, final boolean looped) {
        if (looped) {
            clip.loop(Integer.MAX_VALUE);
        } else {
            clip.start();
        }
    }

    @Override
    public int activeCount(Sound sound) {
        return reverseLookup(sound).size();
    }

}
