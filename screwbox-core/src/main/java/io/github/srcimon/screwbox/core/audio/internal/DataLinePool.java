package io.github.srcimon.screwbox.core.audio.internal;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.ArrayList;
import java.util.List;

public class DataLinePool {


    private class Line {
        private final SourceDataLine line;
        private boolean active;

        public Line(SourceDataLine line) {
            this.line = line;
            this.active = false;
        }
    }

    //TODO implememt max lines cap (free old unused lines)
    private final List<Line> lines = new ArrayList<>();//TODO optimize searching for free line

    public void freeLine(SourceDataLine sourceDataLine) {
        synchronized (lines) {
            lines.stream().filter(line -> line.line.equals(sourceDataLine))
                    .findFirst().orElseThrow()
                    .active = false;
        }
    }

    public SourceDataLine getLine(final AudioFormat format) {
        synchronized (lines) {
            Line lineToUse = lines.stream()
                    .filter(line -> isSame(line.line.getFormat(), format))
                    .filter(line -> !line.active)
                    .findFirst()
                    .orElseGet(() -> startNewLine(format));
            lineToUse.active = true;
            return lineToUse.line;
        }
    }

    public void createLine(final AudioFormat format) {
        startNewLine(format);
    }

    private boolean isSame(AudioFormat format, AudioFormat other) {
        return format.getFrameSize() == other.getFrameSize()
                && format.getEncoding().equals(other.getEncoding())
                && format.getFrameRate() == other.getFrameRate()
                && format.getSampleSizeInBits() == other.getSampleSizeInBits()
                && format.getChannels() == other.getChannels();
    }

    private Line startNewLine(final AudioFormat format) {
        synchronized (lines) {
            try {
                final DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
                sourceDataLine.open(format);
                sourceDataLine.start();
                Line line = new Line(sourceDataLine);
                lines.add(line);
                return line;
            } catch (LineUnavailableException e) {
                throw new IllegalStateException("could not obtain new source data line", e);
            }
        }
    }
}