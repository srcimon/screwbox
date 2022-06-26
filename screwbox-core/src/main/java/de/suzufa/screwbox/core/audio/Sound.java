package de.suzufa.screwbox.core.audio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import de.suzufa.screwbox.core.utils.ResourceLoader;

public final class Sound {

    private final boolean isLooped;
    private final Clip clip;

    public static Sound fromFile(final String fileName) {
        return new Sound(ResourceLoader.loadResource(fileName), false);
    }

    public static Sound fromFileLooped(final String fileName) {
        return new Sound(ResourceLoader.loadResource(fileName), true);
    }

    Sound(byte[] content, final boolean isLooped) {
        try (InputStream inputStream = new ByteArrayInputStream(content)) {
            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream)) {
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
            }
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            throw new IllegalStateException("could not create sound", e);
        }
        this.isLooped = isLooped;
    }

    public boolean isLooped() {
        return isLooped;
    }

    public boolean isActive() {
        return clip.isActive();
    }

    public Clip getClip() {
        return clip;
    }

}
