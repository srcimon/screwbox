package io.github.srcimon.screwbox.core.audio.internal;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.HashMap;
import java.util.Map;

public class DataLinePool {

    //TODO implememt max lines cap (free old unused lines)
    private final Map<SourceDataLine, Boolean> lines = new HashMap<>();
    private int maxLineCount;

    public DataLinePool(final int maxLineCount) {
        this.maxLineCount = maxLineCount;
    }

    public void setMaxLineCount(final int maxLineCount) {
        this.maxLineCount = maxLineCount;
    }

    public void freeLine(SourceDataLine sourceDataLine) {
        synchronized (lines) {
            lines.put(sourceDataLine, false);
        }
    }

    public SourceDataLine getLine(final AudioFormat format) {
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

    public void createLine(final AudioFormat format) {
        startNewLine(format);
    }

    public int size() {
        return lines.size();
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
            try {
                final DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
                sourceDataLine.open(format);
                sourceDataLine.start();
                lines.put(sourceDataLine, false);
                return sourceDataLine;
            } catch (LineUnavailableException e) {
                throw new IllegalStateException("could not obtain new source data line", e);
            }
        }
    }

}