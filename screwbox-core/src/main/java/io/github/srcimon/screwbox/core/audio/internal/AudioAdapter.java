package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.Sound;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AudioAdapter {

    Clip createClip(final Sound sound) {
        try (AudioInputStream audioInputStream = getAudioInputStream(sound.content())) {
            final Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            return clip;
        } catch (LineUnavailableException | IOException e) {
            throw new IllegalStateException("could not create sound", e);
        }
    }

    public TargetDataLine getStartedTargetDataLine(final AudioFormat format) {
        try {
            final var info = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            return line;
        } catch (final LineUnavailableException e) {
            throw new IllegalStateException("error creating audio target line", e);
        }
    }

    void setVolume(final SourceDataLine line, final Percent volume) {
        final FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume.value()));
    }

    void setPan(final SourceDataLine line, final double pan) {
        final FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.PAN);
        gainControl.setValue((float) pan);
    }

    void setBalance(final SourceDataLine line, final double balance) {
        final FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.BALANCE);
        gainControl.setValue((float) balance);
    }

    public static AudioInputStream getAudioInputStream(final byte[] content) {
        try (final var inputStream = new ByteArrayInputStream(content)) {
            return AudioSystem.getAudioInputStream(inputStream);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new IllegalArgumentException("could not load audio content", e);
        }
    }

    public static AudioFormat getAudioFormat(final byte[] content) {
        try (final var inputStream = getAudioInputStream(content)) {
            return inputStream.getFormat();
        } catch (IOException e) {
            throw new IllegalArgumentException("could not get audio format", e);
        }
    }

    public static byte[] convertToStereo(final AudioInputStream monoInputStream) {
        final var originalFormat = monoInputStream.getFormat();
        final var stereoFormat = new AudioFormat(originalFormat.getEncoding(), originalFormat.getSampleRate(),
                originalFormat.getSampleSizeInBits(), 2, originalFormat.getFrameSize() * 2, originalFormat.getFrameRate(), false);
        try (final ByteArrayOutputStream stereoOutputStream = new ByteArrayOutputStream();
             final AudioInputStream stereoInputStream = AudioSystem.getAudioInputStream(stereoFormat, monoInputStream)) {
            AudioSystem.write(stereoInputStream, AudioFileFormat.Type.WAVE, stereoOutputStream);
            return stereoOutputStream.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("could not convert mono to stereo audio", e);
        }
    }

}
