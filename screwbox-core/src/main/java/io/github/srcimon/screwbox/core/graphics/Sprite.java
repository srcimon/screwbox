package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.assets.Asset;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class Sprite implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Sprite INVISIBLE = new Sprite(List.of(Frame.invisible()));

    private final List<Frame> frames = new ArrayList<>();
    private final Time started = Time.now();
    private final Dimension size;
    private final Duration duration;

    public Sprite(Frame frame) {
        this(List.of(frame));
    }

    public Sprite(final List<Frame> frames) {
        if (frames.isEmpty()) {
            throw new IllegalArgumentException("can not create Sprite without frames");
        }
        this.size = frames.get(0).size();

        Duration animationDuration = Duration.none();
        for (final var frame : frames) {
            if (!size.equals(frame.size())) {
                throw new IllegalArgumentException("Cannot add frame with different dimension to sprite");
            }
            animationDuration = animationDuration.add(frame.duration());
            this.frames.add(frame);
        }
        this.duration = animationDuration;
    }

    /**
     * Creates a {@link Sprite} with a single {@link Frame} from the given image file in the class path.
     */
    public static Sprite fromFile(final String fileName) {
        final var frame = Frame.fromFile(fileName);
        return new Sprite(frame);
    }

    /**
     * Creates an {@link Asset} for a {@link Sprite} with a single {@link Frame} from the given image file in the class path.
     */
    public static Asset<Sprite> assetFromFile(final String filename) {
        return Asset.asset(() -> fromFile(filename));
    }

    public static List<Sprite> multipleFromFile(final String fileName, final Dimension dimension) {
        final var sprites = new ArrayList<Sprite>();
        for (final var frame : extractFrames(fileName, dimension)) {
            sprites.add(new Sprite(frame));
        }
        return sprites;
    }


    public static Sprite animatedFromFile(final String fileName, final Dimension dimension, final Duration duration) {
        final var frames = new ArrayList<Frame>();
        for (final var frame : extractFrames(fileName, dimension)) {
            frames.add(new Frame(frame.image(), duration));
        }
        return new Sprite(frames);
    }

    private Sprite(final Image image) {
        this(Frame.fromImage(image));
    }

    private static List<Frame> extractFrames(final String fileName, final Dimension dimension) {
        final var frame = Frame.fromFile(fileName);
        final var extracted = new ArrayList<Frame>();
        for (int y = 0; y + dimension.height() <= frame.size().height(); y += dimension.height()) {
            for (int x = 0; x + dimension.width() <= frame.size().width(); x += dimension.width()) {
                final var area = frame.extractArea(Offset.at(x,y), dimension);
                extracted.add(area);
            }
        }
        return extracted;
    }

    /**
     * Returns an invisible {@link Sprite}.
     */
    public static Sprite invisible() {
        return INVISIBLE;
    }

    /**
     * Returns a {@link Sprite} with one {@link Frame} containing the given
     * {@link Image}.
     */
    public static Sprite fromImage(final Image image) {
        return new Sprite(image);
    }

    /**
     * Returns the count of {@link Frame}s in the {@link Sprite}.
     */
    public int frameCount() {
        return frames.size();
    }

    /**
     * Returns the size of the {@link Sprite}. Every {@link Frame} in the sprites
     * animation has the same size.
     */
    public Dimension size() {
        return size;
    }

    /**
     * Returns the {@link Image} for the given {@link Time}.
     */
    public Image image(final Time time) {
        return frame(time).image();
    }

    /**
     * Returns the {@link Frame} for the given {@link Time}.
     */
    public Frame frame(final Time time) {
        final var frameNr = calculateCurrentFrame(time);
        return frames.get(frameNr);
    }

    /**
     * Returns all {@link Frame}s contained in this {@link Sprite}.
     */
    public List<Frame> allFrames() {
        return frames;
    }

    /**
     * Returns a new {@link Sprite} withe the same {@link Frame}s. But the animation
     * is resetted to {@link Time#now()}.
     */
    public Sprite freshInstance() {
        return new Sprite(frames);
    }

    public Duration duration() {
        return duration;
    }

    /**
     * Returns the frame of the sprite if there is only a single frame in the
     * sprite.
     */
    public Frame singleFrame() {
        if (frameCount() > 1) {
            throw new IllegalStateException("The sprite has more than one frame.");
        }
        return frame(0);
    }

    /**
     * Returns the frame with the given number, starting with 0.
     *
     * @throws IllegalArgumentException if the number is invalid
     */
    public Frame frame(final int nr) {
        if (nr < 0) {
            throw new IllegalArgumentException(nr + " is an invalid frame number");
        }
        if (nr >= frames.size()) {
            throw new IllegalArgumentException(
                    format("Cannot return frame nr %d, because sprite has only %d frame(s).", nr, frames.size()));
        }
        return frames.get(nr);
    }

    /**
     * Returns a new {@link Sprite}. The old {@link Color} in alle {@link Frame}s is
     * replaced with a new one. This method is quite slow.
     *
     * @see Frame#replaceColor(Color, Color)
     */
    public Sprite replaceColor(final Color oldColor, final Color newColor) {
        final List<Frame> recoloredFrames = new ArrayList<>();
        for (final var frame : frames) {
            recoloredFrames.add(frame.replaceColor(oldColor, newColor));
        }
        return new Sprite(recoloredFrames);
    }

    /**
     * Returns a scaled version of this {@link Sprite}.
     */
    public Sprite scaled(final double scale) {
        final List<Frame> scaledFrames = new ArrayList<>();
        for (final var frame : this.frames) {
            scaledFrames.add(frame.scaled(scale));
        }
        return new Sprite(scaledFrames);
    }

    private int calculateCurrentFrame(final Time time) {
        if (frames.size() == 1) {
            return 0;
        }
        final long timerIndex = Duration.between(time, started).nanos() % duration.nanos();
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
}