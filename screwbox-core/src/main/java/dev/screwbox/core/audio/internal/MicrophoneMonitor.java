package dev.screwbox.core.audio.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.audio.AudioConfiguration;
import dev.screwbox.core.utils.internal.ThreadSupport;

import javax.sound.sampled.AudioFormat;
import java.util.concurrent.ExecutorService;

public class MicrophoneMonitor {

    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000, 16, 1, 2, 100, false);
    private final ExecutorService executor;
    private final AudioAdapter audioAdapter;
    private final AudioConfiguration configuration;

    private Percent level = Percent.zero();
    private boolean isActive = false;
    private boolean isUsed = false;
    private Time lastUsed = Time.now();

    public MicrophoneMonitor(final ExecutorService executor, final AudioAdapter audioAdapter, final AudioConfiguration configuration) {
        this.executor = executor;
        this.configuration = configuration;
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
        try (final var line = audioAdapter.createTargetLine(AUDIO_FORMAT)) {
            lastUsed = Time.now();
            final byte[] buffer = new byte[line.getBufferSize()];

            while (!executor.isShutdown() && !isIdleForTooLong()) {
                if (isUsed) {
                    isUsed = false;
                    lastUsed = Time.now();
                }
                ThreadSupport.beNiceToCpu();
                
                final int read = line.available();
                if (line.read(buffer, 0, read) > 0) {
                    this.level = calculateLoudness(buffer, read);
                }
            }
        }
        this.isActive = false;
    }

    //TODO changelog: reduced microphone level latency from about 120ms to 10ms
    private Percent calculateLoudness(final byte[] buffer, int max) {
        double sum = 0;
        int x = 0;
        for (final byte data : buffer) {
            if (x < max) {
                sum = sum + data;
            }
        }

        final double average = sum / buffer.length;
        double sumMeanSquare = 0;

        for (final byte data : buffer) {
            if (x < max) {
                sumMeanSquare += Math.pow(data - average, 2d);
            }
        }

        double averageMeanSquare = sumMeanSquare / max;
        return Percent.of((Math.pow(averageMeanSquare, 0.5)) / 128.0);
    }

    private boolean isIdleForTooLong() {
        final Time timeout = configuration.microphoneIdleTimeout().addTo(lastUsed);
        return Time.now().isAfter(timeout);
    }

}
