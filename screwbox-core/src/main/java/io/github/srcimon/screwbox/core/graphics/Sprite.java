package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.graphics.drawoptions.ShaderSetup;
import io.github.srcimon.screwbox.core.graphics.internal.AwtMapper;
import io.github.srcimon.screwbox.core.graphics.internal.GifFileWriter;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class Sprite implements Serializable, Sizeable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Sprite INVISIBLE = new Sprite(List.of(Frame.invisible()));
    private final List<Frame> frames = new ArrayList<>();
    private final Time started = Time.now();
    private final Size size;
    private final Duration duration;

    public Sprite(Frame frame) {
        this(List.of(frame));
    }

    public Sprite(final List<Frame> frames) {
        if (frames.isEmpty()) {
            throw new IllegalArgumentException("can not create Sprite without frames");
        }
        this.size = frames.getFirst().size();

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

    public static Sprite placeholder(final Color color, final int size) {
        return placeholder(color, Size.square(size));
    }

    /**
     * Creates a placeholder {@link Sprite} with a single {@link Frame}. Used when to lazy to create custom images.
     *
     * @since 2.11.0
     */
    public static Sprite placeholder(final Color color, final Size size) {
        requireNonNull(color, "color must not be null");
        requireNonNull(size, "size must not be null");
        Validate.isTrue(size::isValid, "size must be valid");
        final var image = new BufferedImage(size.width(), size.height(), BufferedImage.TYPE_INT_ARGB);
        final var graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(AwtMapper.toAwtColor(color));
        graphics.fillRect(1, 1, size.width() - 2, size.height() - 2);

        graphics.setColor(AwtMapper.toAwtColor(color).brighter());
        graphics.drawLine(0, 0, size.width(), 0);
        graphics.drawLine(1, 1, 1, 1);
        graphics.drawLine(0, 1, 0, size.height() - 1);

        graphics.setColor(AwtMapper.toAwtColor(color).darker());
        graphics.drawLine(1, size.height() - 1, size.width() - 1, size.height() - 1);
        graphics.drawLine(size.width() - 2, size.height() - 2, size.width() - 2, size.height() - 2);
        graphics.drawLine(size.width() - 1, 1, size.width() - 1, size.height() - 2);
        graphics.dispose();

        return Sprite.fromImage(image);
    }

    /**
     * Creates an {@link Asset} for a {@link Sprite} with a single {@link Frame} from the given image file in the class path.
     */
    public static Asset<Sprite> assetFromFile(final String filename) {
        return Asset.asset(() -> fromFile(filename));
    }

    public static List<Sprite> multipleFromFile(final String fileName, final Size size) {
        final var sprites = new ArrayList<Sprite>();
        for (final var frame : extractFrames(fileName, size)) {
            sprites.add(new Sprite(frame));
        }
        return sprites;
    }

    /**
     * Creates a {@link Sprite} with a single {@link Frame} containing a single pixel of the
     * specified {@link Color}.
     */
    public static Sprite pixel(final Color color) {
        final var image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, AwtMapper.toAwtColor(color).getRGB());
        return Sprite.fromImage(image);
    }

    public static Asset<Sprite> animatedAssetFromFile(final String fileName, final Size size, final Duration duration) {
        return Asset.asset(() -> animatedFromFile(fileName, size, duration));
    }

    public static Sprite animatedFromFile(final String fileName, final Size size, final Duration duration) {
        final var frames = new ArrayList<Frame>();
        for (final var frame : extractFrames(fileName, size)) {
            frames.add(new Frame(frame.image(), duration));
        }
        return new Sprite(frames);
    }

    private Sprite(final Image image) {
        this(Frame.fromImage(image));
    }

    private static List<Frame> extractFrames(final String fileName, final Size size) {
        final var frame = Frame.fromFile(fileName);
        final var extracted = new ArrayList<Frame>();
        for (int y = 0; y + size.height() <= frame.height(); y += size.height()) {
            for (int x = 0; x + size.width() <= frame.width(); x += size.width()) {
                final var area = frame.extractArea(Offset.at(x, y), size);
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
    public Size size() {
        return size;
    }

    /**
     * Returns the {@link Image} for the given {@link Time}.
     */
    public Image image(final Time time) {
        return frame(time).image();
    }

    /**
     * Returns the {@link Frame} for the specified {@link Time}.
     */
    public Frame frame(final Time time) {
        if (frames.size() == 1) {
            return frames.getFirst();
        }
        final long timerIndex = Duration.between(time, started).nanos() % duration.nanos();
        long sumAtCurrentIndex = 0;
        long sumAtNextIndex = 0;
        for (final Frame frame : frames) {
            sumAtNextIndex += frame.duration().nanos();

            if (timerIndex >= sumAtCurrentIndex && timerIndex <= sumAtNextIndex) {
                return frame;
            }

            sumAtCurrentIndex += frame.duration().nanos();
        }
        return frames.getLast();
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
     * Returns the {@link Frame} of the {@link Sprite} if there is only a single {@link Frame} in the {@link Sprite}.
     *
     * @throws IllegalStateException {@link Sprite} has more than one {@link Frame}
     */
    public Frame singleFrame() {
        if (frameCount() > 1) {
            throw new IllegalStateException("the sprite has more than one frame");
        }
        return frame(0);
    }

    /**
     * Returns the {@link Image} of the {@link Sprite} if there is only a single {@link Frame} in the {@link Sprite}.
     *
     * @throws IllegalStateException {@link Sprite} has more than one {@link Frame}
     */
    public Image singleImage() {
        return singleFrame().image();
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
                    "Cannot return frame nr %d, because sprite has only %d frame(s).".formatted(nr, frames.size()));
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

    /**
     * Returns a version of this {@link Sprite} where the transparent space on the left and the right side were reduced to a minimum.
     *
     * @throws IllegalArgumentException on resulting {@link Frame}s have different sizes
     */
    public Sprite cropHorizontal() {
        final List<Frame> croppedFrames = new ArrayList<>();
        for (final var frame : this.frames) {
            croppedFrames.add(frame.cropHorizontal());
        }
        return new Sprite(croppedFrames);
    }

    /**
     * Returns a new {@link Sprite} with border of specified width and color.
     *
     * @since 2.15.0
     */
    public Sprite addBorder(final int width, final Color color) {
        final List<Frame> adjustedFrames = new ArrayList<>();
        for (final var frame : this.frames) {
            adjustedFrames.add(frame.addBorder(width, color));
        }
        return new Sprite(adjustedFrames);
    }


    public Sprite prepareShader(Supplier<ShaderSetup> shaderOptions) {
        return prepareShader(shaderOptions.get().shader());
    }

    public Sprite prepareShader(Shader shader) {
        for (final var frame : frames) {
            frame.prepareShader(shader);
        }
        return this;
    }

    /**
     * Exports the sprite as gif file at the specified location.
     *
     * @since 2.15.0
     */
    public void exportGif(final String fileName) {
        requireNonNull(fileName, "file name must not be null");
        final String exportName = fileName.endsWith(".gif") ? fileName : fileName + ".gif";

        try (final var gifFileWriter = new GifFileWriter(exportName)) {
            for (final var frame : allFrames()) {
                final var image = ImageUtil.toBufferedImage(frame.image());
                gifFileWriter.addImage(image, frame.duration());
            }
        }
    }

    /**
     * Returns the image matching the specified {@link ShaderSetup} at the specified {@link Time}.
     *
     * @since 2.15.0
     */
    public Image image(final ShaderSetup shaderSetup, final Time time) {
        return frame(time).image(shaderSetup, time);
    }
}