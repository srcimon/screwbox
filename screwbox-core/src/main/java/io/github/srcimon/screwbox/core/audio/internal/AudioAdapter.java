package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.Sound;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AudioAdapter {

    Clip createClip(final Sound sound, final Percent volume) {
        try (AudioInputStream audioInputStream = getAudioInputStream(sound)) {
            final Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            setVolume(clip, volume);
            return clip;
        } catch (LineUnavailableException | IOException e) {
            throw new IllegalStateException("could not create sound", e);
        }
    }

    void setVolume(final Clip clip, final Percent volume) {
        final FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume.value()));
    }

    Duration getDuration(final Sound sound) {
        try (AudioInputStream audioInputStream = getAudioInputStream(sound)) {
            var length = 1000.0 * audioInputStream.getFrameLength() / audioInputStream.getFormat().getFrameRate();
            return Duration.ofMillis((int) length);
        } catch (IOException e) {
            throw new IllegalStateException("could not create sound", e);
        }
    }

    private AudioInputStream getAudioInputStream(final Sound sound) {
        try (InputStream inputStream = new ByteArrayInputStream(sound.content())) {
            return AudioSystem.getAudioInputStream(inputStream);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new IllegalStateException("audio format unsupported", e);
        }
    }
}
