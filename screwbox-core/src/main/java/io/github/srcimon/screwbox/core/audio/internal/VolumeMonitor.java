package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.util.concurrent.ExecutorService;

public class VolumeMonitor {

    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000, 16, 1, 2, 100, false);
    private final ExecutorService executor;
    private final AudioAdapter audioAdapter;

    private Percent level = Percent.zero();
    private boolean isActive = false;
    private boolean isUsed = false;
    private Time lastUsed = Time.now();
    private Duration idleTimeout;

    public VolumeMonitor(final ExecutorService executor, final AudioAdapter audioAdapter) {
        this.executor = executor;
        this.audioAdapter = audioAdapter;
    }

    public void start(final Duration idleTimeout) {
        this.idleTimeout = idleTimeout;
        isActive = true;
        executor.execute(this::continuouslyMonitorMicrophoneLevel);
    }

    public Percent level() {
        isUsed = true;
        return level;
    }

    private void continuouslyMonitorMicrophoneLevel() {
        final var info = new DataLine.Info(TargetDataLine.class, AUDIO_FORMAT);
        try (final var line = audioAdapter.getTargetDataLine(info)) {
            line.open(AUDIO_FORMAT);
            line.start();
            final byte[] buffer = new byte[8];

            while (!executor.isShutdown() && !isIdleForTooLong()) {
                if (isUsed) {
                    isUsed = false;
                    lastUsed = Time.now();
                }
                if (line.read(buffer, 0, buffer.length) > 0) {
                    this.level = calculateRMSLevel(buffer);
                }
            }
        } catch (LineUnavailableException e) {
            throw new RuntimeException("error listening to microphone", e);
        }
        isActive = false;
    }

    private Percent calculateRMSLevel(final byte[] buffer) {
        long sum = 0;
        for (final byte data : buffer) {
            sum = sum + data;
        }

        final double average = sum / buffer.length;
        double sumMeanSquare = 0;

        for (final byte data : buffer) {
            sumMeanSquare += Math.pow(data - average, 2d);
        }

        double averageMeanSquare = sumMeanSquare / buffer.length;
        return Percent.of((Math.pow(averageMeanSquare, 0.5) + 0.5) / 100.0);
    }

    public boolean isActive() {
        return isActive;
    }

    private boolean isIdleForTooLong() {
        return Time.now().isAfter(lastUsed.plus(idleTimeout));
    }

}
