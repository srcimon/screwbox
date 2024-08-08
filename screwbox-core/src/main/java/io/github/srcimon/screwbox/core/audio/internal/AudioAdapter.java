package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AudioAdapter {

    public static void setVolume(final SourceDataLine line, final Percent volume) {
        final FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);

        gainControl.setValue(Math.clamp(20f * (float) Math.log10(volume.value()), gainControl.getMinimum(), gainControl.getMaximum()));
    }

    public static void setPan(final SourceDataLine line, final double pan) {
        final FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.PAN);
        gainControl.setValue((float) Math.clamp(pan, gainControl.getMinimum(), gainControl.getMaximum()));
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

    public SourceDataLine createSourceLine(final AudioFormat format) {
        try {
            final var info = new DataLine.Info(SourceDataLine.class, format);
            final var sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceDataLine.open(format);
            sourceDataLine.start();
            return sourceDataLine;
        } catch (LineUnavailableException e) {
            throw new IllegalStateException("could not obtain new source data line", e);
        }
    }

    public TargetDataLine createTargetLine(final AudioFormat format) {
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
}
