package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.audio.AudioConfiguration;

import javax.sound.sampled.AudioFormat;
import java.util.concurrent.ExecutorService;

public class VolumeMonitor {

    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000, 16, 1, 2, 100, false);
    private final ExecutorService executor;
    private final AudioAdapter audioAdapter;
    private final AudioConfiguration coniguration;

    private Percent level = Percent.zero();
    private boolean isActive = false;
    private boolean isUsed = false;
    private Time lastUsed = Time.now();

    public VolumeMonitor(final ExecutorService executor, final AudioAdapter audioAdapter, final AudioConfiguration configuration) {
        this.executor = executor;
        this.coniguration = configuration;
        this.audioAdapter = audioAdapter;
    }

    public Percent level() {
        isUsed = true;
        if (!isActive) {
            isActive = true;
            executor.execute(this::continuouslyMonitorMicrophoneLevel);
        }
        return level;
    }

    public boolean isActive() {
        synchronized (this) {
            return isActive;
        }
    }

    private void continuouslyMonitorMicrophoneLevel() {
        try (final var line = audioAdapter.getStartedTargetDataLine(AUDIO_FORMAT)) {
            lastUsed = Time.now();
            final byte[] buffer = new byte[line.getBufferSize()];

            while (!executor.isShutdown() && !isIdleForTooLong()) {
                if (isUsed) {
                    isUsed = false;
                    lastUsed = Time.now();
                }

                if (line.read(buffer, 0, line.getBufferSize()) > 0) {
                    this.level = calculadeLoudness(buffer);
                }
            }
        }
        this.isActive = false;
    }

    private Percent calculadeLoudness(final byte[] buffer) {
        double sum = 0;
        for (final byte data : buffer) {
            sum = sum + data;
        }

        final double average = sum / buffer.length;
        double sumMeanSquare = 0;

        for (final byte data : buffer) {
            sumMeanSquare += Math.pow(data - average, 2d);
        }

        double averageMeanSquare = sumMeanSquare / buffer.length;
        return Percent.of((Math.pow(averageMeanSquare, 0.5)) / 128.0);
    }

    private boolean isIdleForTooLong() {
        final Time timeout = coniguration.microphoneIdleTimeout().addTo(lastUsed);
        return Time.now().isAfter(timeout);
    }

}
