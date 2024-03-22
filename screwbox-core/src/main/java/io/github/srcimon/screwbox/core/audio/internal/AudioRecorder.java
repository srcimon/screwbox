package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class AudioRecorder {
    private double level = 0;
    private boolean isRunning = true;

    public void start() {
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000, 16, 1, 2, 100, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        try {
            var line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            byte tempBuffer[] = new byte[10];
            while (isRunning) {
                if (line.read(tempBuffer, 0, tempBuffer.length) > 0) {
                    var level = calculateRMSLevel(tempBuffer);
                    this.level = level / 70.0;

                }
            }
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private int calculateRMSLevel(byte[] audioData) {
        long lSum = 0;
        for (int i = 0; i < audioData.length; i++)
            lSum = lSum + audioData[i];

        double dAvg = lSum / audioData.length;
        double sumMeanSquare = 0d;

        for (int j = 0; j < audioData.length; j++)
            sumMeanSquare += Math.pow(audioData[j] - dAvg, 2d);

        double averageMeanSquare = sumMeanSquare / audioData.length;

        return (int) (Math.pow(averageMeanSquare, 0.5d) + 0.5);
    }

    public Percent level() {
        return Percent.of(level);
    }

    public void stop() {
        isRunning = false;
    }
}
