package io.github.srcimon.screwbox.core.audio.internal;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;
import java.util.HashMap;
import java.util.Map;

public class AudioLinePool {

    //TODO implememt max lines cap (free old unused lines)
    private final Map<SourceDataLine, Boolean> lines = new HashMap<>();
    private final AudioAdapter audioAdapter;

    public AudioLinePool(final AudioAdapter audioAdapter) {
        this.audioAdapter = audioAdapter;
    }

    public void prepareLine(final AudioFormat format) {
        startNewLine(format);
    }

    public void releaseLine(final SourceDataLine sourceDataLine) {
        synchronized (lines) {
            lines.put(sourceDataLine, false);
        }
    }

    public int size() {
        return lines.size();
    }

    public SourceDataLine aquireLine(final AudioFormat format) {
        synchronized (lines) {
            var sourceDataLine = lines.entrySet()
                    .stream()
                    .filter(line -> !line.getValue())
                    .filter(line -> isSame(line.getKey().getFormat(), format))
                    .findFirst().map(line -> line.getKey())
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
            var line = audioAdapter.createLine(format);
            lines.put(line, false);
            return line;
        }
    }

}