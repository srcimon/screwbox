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
        private AudioFormat format;
        private boolean active;
        private SourceDataLine line;

        public Line(AudioFormat format, SourceDataLine line) {
            this.format = format;
            this.line = line;
            this.active = false;
        }
    }

    private List<Line> linePool = new ArrayList<>();//TODO optimize searching for free line

    public void freeLine(SourceDataLine sourceDataLine) {
        synchronized (linePool) {
            System.out.println("freed data line");
            linePool.stream().filter(line -> line.line.equals(sourceDataLine))
                    .findFirst().orElseThrow()
                    .active = false;
        }
    }

    public SourceDataLine getLine(final AudioFormat format) {
        synchronized (linePool) {
            Line lineToUse = linePool.stream()
                    .filter(line -> sameFormat(line.format, format))
                    .filter(line -> !line.active)
                    .findFirst()
                    .orElseGet(() -> createAndAddLine(format));
            lineToUse.active = true;
            return lineToUse.line;
        }
    }

    private boolean sameFormat(AudioFormat a, AudioFormat b) {
        return a.getFrameSize() == b.getFrameSize()
                && a.getEncoding().equals(b.getEncoding())
                && a.getFrameRate() == b.getFrameRate()
                && a.getSampleSizeInBits() == b.getSampleSizeInBits()
                && a.getChannels() == b.getChannels();
    }

    private Line createAndAddLine(AudioFormat format) {
        Line line = new Line(format, createLine(format));
        linePool.add(line);
        System.out.println("added new line to pool, poolsize is: " + linePool.size());
        return line;
    }

    private SourceDataLine createLine(final AudioFormat format) {
        try {
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceDataLine.open(format);
            sourceDataLine.start();
            return sourceDataLine;
        } catch (LineUnavailableException e) {
            throw new IllegalStateException("could not obtain new source data line", e);
        }
    }
}
