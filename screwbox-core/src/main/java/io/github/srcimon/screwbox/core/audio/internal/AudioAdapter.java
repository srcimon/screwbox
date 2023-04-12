package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.Sound;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AudioAdapter {

    public Clip createClip(final Sound sound, final Percent volume) {
        try (InputStream inputStream = new ByteArrayInputStream(sound.content())) {
            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream)) {
                final Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                final FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(20f * (float) Math.log10(volume.value()));
                return clip;
            }
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            throw new IllegalStateException("could not create sound", e);
        }
    }

}
