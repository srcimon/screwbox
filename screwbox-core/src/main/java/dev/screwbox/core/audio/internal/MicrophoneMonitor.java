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
            Time timeout = configuration.microphoneIdleTimeout().addTo(Time.now());
            final byte[] buffer = new byte[line.getBufferSize()];

            while (!executor.isShutdown() && !Time.now().isAfter(timeout)) {
                if (isUsed) {
                    isUsed = false;
                    timeout = configuration.microphoneIdleTimeout().addTo(Time.now());
                }
                ThreadSupport.beNiceToCpu();

                final int read = Math.min(line.available(), buffer.length);
                if (line.read(buffer, 0, read) > 0) {
                    this.level = calculateLoudness(buffer, read);
                }
            }
        }
        this.isActive = false;
    }

    private static Percent calculateLoudness(final byte[] buffer, final int bytesRead) {
        double sum = 0;
        for (int i = 0; i < bytesRead; i++) {
            sum = sum + buffer[i];
        }

        final double average = sum / buffer.length;

        double sumMeanSquare = 0;
        for (int i = 0; i < bytesRead; i++) {
            sumMeanSquare += Math.pow(buffer[i] - average, 2d);
        }

        final double averageMeanSquare = sumMeanSquare / bytesRead;

        return Percent.of((Math.pow(averageMeanSquare, 0.5)) / 64.0);
    }

}
