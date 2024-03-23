package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.audio.AudioConfiguration;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.util.concurrent.ExecutorService;

public class VolumeMonitor {

    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000, 16, 1, 2, 100, false);
    private final ExecutorService executor;
    private final AudioConfiguration configuration;

    private Percent level = Percent.zero();
    private boolean isActive = false;
    private boolean isUsed = false;
    private Time lastUsed = Time.now();

    public VolumeMonitor(final ExecutorService executor, final AudioConfiguration configuration) {
        this.executor = executor;
        this.configuration = configuration;
    }

    public Percent level() {
        isUsed = true;
        if (!isActive) {
            isActive = true;
            executor.execute(this::continuouslyMonitorMicrophoneLevel);
        }
        return level;
    }

    private void continuouslyMonitorMicrophoneLevel() {
        final var info = new DataLine.Info(TargetDataLine.class, AUDIO_FORMAT);
        try (final var line = (TargetDataLine) AudioSystem.getLine(info)) {
            line.open(AUDIO_FORMAT);
            line.start();
            final byte[] buffer = new byte[8];

            while (!executor.isShutdown() && !isIdleForTooLong()) {
                if (isUsed) {
                    isUsed = false;
                    lastUsed = Time.now();
                }
                if (line.read(buffer, 0, buffer.length) > 0) {
                    this.level = AudioAdapter.calculateRMSLevel(buffer);
                }
            }
        } catch (LineUnavailableException e) {
            throw new RuntimeException("error listening to microphone", e);
        }
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    private boolean isIdleForTooLong() {
        return Time.now().isAfter(lastUsed.plus(configuration.microphoneIdleTimeout()));
    }
}
