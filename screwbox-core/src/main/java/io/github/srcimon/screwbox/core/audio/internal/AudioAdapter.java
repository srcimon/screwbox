package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.audio.Sound;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
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

    void setVolume(final Clip clip, final Percent volume) {
        final FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume.value()));
    }

    void setPan(final Clip clip, final double pan) {
        final FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.PAN);
        gainControl.setValue((float) pan);
    }

    void setBalance(final Clip clip, final double balance) {
        final FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.BALANCE);
        gainControl.setValue((float) balance);
    }

    //TODO Refactor test and
    public static Percent calculateRMSLevel(final byte[] audioData) {
        long lSum = 0;
        for (int i = 0; i < audioData.length; i++)
            lSum = lSum + audioData[i];

        double dAvg = lSum / audioData.length;
        double sumMeanSquare = 0;

        for (int j = 0; j < audioData.length; j++)
            sumMeanSquare += Math.pow(audioData[j] - dAvg, 2d);

        double averageMeanSquare = sumMeanSquare / audioData.length;


        var x = (int) (Math.pow(averageMeanSquare, 0.5) + 0.5);
        return Percent.of(x / 70.0);
    }

    public static AudioInputStream getAudioInputStream(final byte[] content) {
        try (InputStream inputStream = new ByteArrayInputStream(content)) {
            return AudioSystem.getAudioInputStream(inputStream);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new IllegalArgumentException("could not load audio content", e);
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
