package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Duration;
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

    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000, 16, 1, 2, 100, false);
    private final ExecutorService executor;
    private final Loop loop;
    private final Log log;

    private Percent level = Percent.zero();
    private boolean timeoutReached = false;
    private boolean isActive = false;
    private Time timeout = Time.now();

    public VolumeMonitor(final ExecutorService executor, final Loop loop, final Log log) {
        this.loop = loop;
        this.log = log;
        this.executor = executor;
    }

    public Percent level(final Duration timeout) {
        this.timeout = loop.lastUpdate().plus(timeout);
        if (!isActive) {
            timeoutReached = false;
            isActive = true;
            executor.execute(this::continuouslyMonitorMicrophoneLevel);
        }
        return level;
    }

    private void continuouslyMonitorMicrophoneLevel() {
        log.debug("started monitoring microphone");
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, AUDIO_FORMAT);
        try (final var line = (TargetDataLine) AudioSystem.getLine(info)) {
            line.open(AUDIO_FORMAT);
            line.start();
            final byte[] tempBuffer = new byte[10];

            while (!executor.isShutdown() && !timeoutReached) {
                if (loop.lastUpdate().isAfter(timeout)) {
                    log.debug("microphone input no longer required");
                    timeoutReached = true;
                }
                if (line.read(tempBuffer, 0, tempBuffer.length) > 0) {
                    this.level = AudioAdapter.calculateRMSLevel(tempBuffer);
                }
            }
        } catch (LineUnavailableException e) {
            throw new RuntimeException("error listening to microphone", e);
        }
        isActive = false;
        log.debug("stopped monitoring microphone");
    }

    public boolean isActive() {
        return isActive;
    }
}
