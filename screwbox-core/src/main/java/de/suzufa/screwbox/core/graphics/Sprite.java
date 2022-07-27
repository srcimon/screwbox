package de.suzufa.screwbox.core.graphics;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.isNull;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.utils.ResourceLoader;

public class Sprite implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Frame> frames = new ArrayList<>();
    private Dimension dimension;
    private final Time started = Time.now();
    private Duration duration = Duration.zero();
    private boolean flippedHorizontally = false;
    private boolean flippedVertically = false;

    private Sprite(final Image image) {
        this(asList(new Frame(image)), false);
    }

    public Sprite(final List<Frame> frames) {
        this(frames, false);
    }

    private Sprite(final List<Frame> frames, final boolean flipped) {
        this.flippedHorizontally = flipped;
        for (final var frame : frames) {
            addFrame(frame);
        }
    }

    public static Sprite fromFile(final String fileName) {
        final var image = imageFromFile(fileName);
        return fromImage(image);
    }

    public static List<Sprite> multipleFromFile(final String fileName, final Dimension dimension) {
        return multipleFromFile(fileName, dimension, 0);
    }

    public static List<Sprite> multipleFromFile(final String fileName, final Dimension dimension, int padding) {
        final var image = imageFromFile(fileName);
        final var sprites = new ArrayList<Sprite>();
        for (int y = 0; y + dimension.height() <= image.getHeight(); y += dimension.height() + padding) {
            for (int x = 0; x + dimension.width() <= image.getWidth(); x += dimension.width() + padding) {
                final var subimage = image.getSubimage(x, y, dimension.width(), dimension.height());
                sprites.add(fromImage(subimage));
            }
        }
        return sprites;
    }

    public static Sprite invisible() {
        return fromImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
    }

    public static Sprite fromImage(final Image image) {
        return new Sprite(image);
    }

    public boolean isFlippedHorizontally() {
        return flippedHorizontally;
    }

    public void setFlippedHorizontally(final boolean flippedHorizontally) {
        this.flippedHorizontally = flippedHorizontally;
    }

    public boolean isFlippedVertically() {
        return flippedVertically;
    }

    public void setFlippedVertically(final boolean flippedVertically) {
        this.flippedVertically = flippedVertically;
    }

    public Dimension size() {
        return dimension;
    }

    public Image getFirstImage() {
        return getImage(0);
    }

    public Image getImage(final Time time) {
        final var frameNr = getFrameNr(frames, duration, time);
        return getImage(frameNr);
    }

    public Sprite newInstance() {
        return new Sprite(frames, flippedHorizontally);
    }

    private Image getImage(final int frameNr) {
        return frames.get(frameNr).image(flippedHorizontally, flippedVertically);
    }

    public Duration duration() {
        return duration;
    }

    /**
     * Returns the frame of the sprite if there is only a single frame in the
     * sprite.
     */
    public Frame singleFrame() {
        if (frames.size() > 1) {
            throw new IllegalStateException("The sprite has more than one frame.");
        }
        return getFrame(0);
    }

    /**
     * Returns the frame with the given number, starting with 0.
     * 
     * @throws IllegalArgumentException if the number is invalid
     */
    public Frame getFrame(final int nr) {
        if (nr < 0) {
            throw new IllegalArgumentException(nr + " is an invalid frame number");
        }
        if (nr >= frames.size()) {
            throw new IllegalArgumentException(
                    format("Cannot return frame nr %d, because sprite has only %d frame(s).", nr, frames.size()));
        }
        return frames.get(nr);
    }

    private void addFrame(final Frame frame) {
        if (isNull(dimension)) {
            dimension = frame.size();
        } else if (!dimension.equals(frame.size())) {
            throw new IllegalArgumentException("Cannot add frame with different dimension to sprite");
        }
        duration = duration.plus(frame.duration());
        frames.add(frame);
    }

    private int getFrameNr(final List<Frame> frames, final Duration durationSum, final Time time) {
        if (frames.size() == 1) {
            return 0;
        }
        final long timerIndex = Duration.between(time, started).nanos() % durationSum.nanos();
        long sumAtCurrentIndex = 0;
        long sumAtNextIndex = 0;
        for (int i = 0; i < frames.size(); i++) {
            sumAtNextIndex += frames.get(i).duration().nanos();

            if (timerIndex >= sumAtCurrentIndex && timerIndex <= sumAtNextIndex) {
                return i;
            }

            sumAtCurrentIndex += frames.get(i).duration().nanos();
        }
        return frames.size() - 1;
    }

    private static BufferedImage imageFromFile(final String fileName) {
        try {
            final File resource = ResourceLoader.resourceFile(fileName);
            final BufferedImage image = ImageIO.read(resource);
            if (isNull(image)) {
                throw new IllegalArgumentException("image cannot be read: " + fileName);
            }
            return image;

        } catch (final IOException e) {
            throw new IllegalArgumentException("error while reading image: " + fileName, e);
        }
    }

}
