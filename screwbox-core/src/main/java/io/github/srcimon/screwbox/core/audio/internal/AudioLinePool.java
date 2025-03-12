package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.audio.AudioConfiguration;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AudioLinePool {

    private final Map<SourceDataLine, Boolean> lines = new ConcurrentHashMap<>();
    private final AudioAdapter audioAdapter;
    private final AudioConfiguration configuration;

    public AudioLinePool(final AudioAdapter audioAdapter, final AudioConfiguration configuration) {
        this.audioAdapter = audioAdapter;
        this.configuration = configuration;
    }

    public void prepareLine(final AudioFormat format) {
        startNewLine(format);
    }

    public void releaseLine(final SourceDataLine sourceDataLine) {
        lines.put(sourceDataLine, false);
    }

    public int size() {
        return lines.size();
    }

    public SourceDataLine acquireLine(final AudioFormat format) {
        synchronized (lines) {
            var sourceDataLine = lines.entrySet()
                    .stream()
                    .filter(line -> !line.getValue())
                    .filter(line -> isSame(line.getKey().getFormat(), format))
                    .findFirst().map(Map.Entry::getKey)
                    .orElseGet(() -> startNewLine(format));
            lines.put(sourceDataLine, true);
            return sourceDataLine;
        }
    }

    private boolean isSame(final AudioFormat format, final AudioFormat other) {
        return format.getFrameSize() == other.getFrameSize()
                && format.getSampleRate() == other.getSampleRate()
                && format.getEncoding().equals(other.getEncoding())
                && format.getFrameRate() == other.getFrameRate()
                && format.getSampleSizeInBits() == other.getSampleSizeInBits()
                && format.getChannels() == other.getChannels();
    }

    private SourceDataLine startNewLine(final AudioFormat format) {
        synchronized (lines) {
            while (size() >= configuration.maxLines()) {
                var lineToRemove = lines.entrySet()
                        .stream()
                        .filter(line -> !line.getValue())
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("audio line pool has reached max capacity of %s lines"
                                .formatted(configuration.maxLines())))
                        .getKey();
                lineToRemove.stop();
                lines.remove(lineToRemove);
            }
            var line = audioAdapter.createSourceLine(format);
            lines.put(line, false);
            return line;
        }
    }

    public List<SourceDataLine> lines() {
        return new ArrayList<>(lines.keySet());
    }
}