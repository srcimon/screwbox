package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.log.Log;
import io.github.srcimon.screwbox.core.loop.Loop;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.util.concurrent.ExecutorService;

public class VolumeMonitor {

    private final ExecutorService executor;
    private final Loop loop;
    private final Log log;

    private double level = 0;
    private boolean mustStop = false;
    private boolean canStop = false;
    private boolean isRunning = false;
    private Time timeout = Time.now();

    public VolumeMonitor(final ExecutorService executor, final Loop loop, final Log log) {
        this.loop = loop;
        this.log = log;
        this.executor = executor;
    }

    public Percent level() {
        timeout = loop.lastUpdate().plusSeconds(5);
        if (!isRunning) {
            canStop = false;
            isRunning = true;
            executor.submit(() -> start());
        }
        return Percent.of(level);
    }

    public void stop() {
        mustStop = true;
    }

    private void start() {
        log.debug("started monitoring microphone");
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000, 16, 1, 2, 100, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        try (TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info)) {
            line.open(format);
            line.start();
            byte tempBuffer[] = new byte[10];

            while (!mustStop && !canStop) {
                if (loop.lastUpdate().isAfter(timeout)) {
                    log.debug("microphone input no longer required");
                    canStop = true;
                }
                if (line.read(tempBuffer, 0, tempBuffer.length) > 0) {
                    var level = calculateRMSLevel(tempBuffer);
                    this.level = level / 70.0;

                }
                throw new IllegalStateException("x");
            }
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        isRunning = false;
        log.debug("stopped monitoring microphone");
    }


    private int calculateRMSLevel(final byte[] audioData) {
        long lSum = 0;
        for (int i = 0; i < audioData.length; i++)
            lSum = lSum + audioData[i];

        double dAvg = lSum / audioData.length;
        double sumMeanSquare = 0;

        for (int j = 0; j < audioData.length; j++)
            sumMeanSquare += Math.pow(audioData[j] - dAvg, 2d);

        double averageMeanSquare = sumMeanSquare / audioData.length;

        return (int) (Math.pow(averageMeanSquare, 0.5) + 0.5);
    }
}
