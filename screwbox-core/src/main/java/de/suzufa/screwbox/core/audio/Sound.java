package de.suzufa.screwbox.core.audio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import de.suzufa.screwbox.core.utils.ResourceLoader;

public final class Sound implements Serializable {

    private static final long serialVersionUID = 1L;

    private final byte[] content;

    public static Sound fromFile(final String fileName) {
        return new Sound(ResourceLoader.loadResource(fileName));
    }

    Sound(byte[] content) {
        this.content = content;
    }

    public Clip getClip() {
        try (InputStream inputStream = new ByteArrayInputStream(content)) {
            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream)) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                return clip;
            }
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            throw new IllegalStateException("could not create sound", e);
        }
    }

}
