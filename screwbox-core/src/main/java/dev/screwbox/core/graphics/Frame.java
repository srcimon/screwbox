package dev.screwbox.core.graphics;

import dev.screwbox.core.scenes.Scene;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.internal.filter.ReplaceColorFilter;
import dev.screwbox.core.utils.Cache;
import dev.screwbox.core.utils.Resources;
import dev.screwbox.core.utils.Validate;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * Represents a single image within a {@link Sprite}. Every {@link Frame} also contains the {@link Duration} of it's
 * visibility within the {@link Sprite}.
 */
public final class Frame implements Serializable, Sizeable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Frame INVISIBLE = new Frame(ImageOperations.createEmpty(Size.square(1)));
    private static final int SHADER_CACHE_LIMIT = 100;

    private final Duration duration;
    private final ImageIcon imageStorage;

    private final Cache<String, Image> shaderCache = new Cache<>();

    /**
     * Returns an invisible {@link Frame}.
     */
    public static Frame invisible() {
        return INVISIBLE;
    }

    /**
     * Returns a {@link Frame} from the given {@link Image}.
     */
    public static Frame fromImage(final Image image) {
        return new Frame(image);
    }

    /**
     * Returns a {@link Frame} created from a file.
     */
    public static Frame fromFile(final String fileName) {
        final byte[] imageData = Resources.loadBinary(fileName);
        try (var inputStream = new ByteArrayInputStream(imageData)) {
            final BufferedImage image = ImageIO.read(inputStream);
            if (isNull(image)) {
                throw new IllegalArgumentException("image cannot be read: " + fileName);
            }
            return new Frame(image);
        } catch (final IOException e) {
            throw new IllegalArgumentException("error while reading image: " + fileName, e);
        }
    }

    private Frame(final Image image) {
        this(image, Duration.none());
    }

    public Frame(final Image image, final Duration duration) {
        this.imageStorage = new ImageIcon(image);
        this.duration = duration;
    }

    /**
     * Returns a new {@link Frame} created from a sub image of this {@link Frame}.
     */
    public Frame extractArea(final Offset offset, final Size size) {
        if (offset.x() < 0
                || offset.y() < 0
                || offset.x() + size.width() > size().width()
                || offset.y() + size.height() > size().height()) {
            throw new IllegalArgumentException("given offset and size are out off frame bounds");
        }
        final var image = ImageOperations.toBufferedImage(image());
        final var subImage = image.getSubimage(offset.x(), offset.y(), size.width(), size.height());
        return new Frame(subImage, duration);
    }

    public Image image() {
        return imageStorage.getImage();
    }

    public Duration duration() {
        return duration;
    }

    /**
     * Returns the size of the frames {@link #image()}.
     */
    @Override
    public Size size() {
        return Size.of(imageStorage.getIconWidth(), imageStorage.getIconHeight());
    }

    /**
     * Returns the {@link Color} of the pixel at the given position.
     *
     * @throws IllegalArgumentException when position is out of bounds.
     * @see #colorAt(int, int)
     */
    public Color colorAt(final Offset offset) {
        return colorAt(offset.x(), offset.y());
    }

    /**
     * Returns the {@link Color} of the pixel at the given position.
     *
     * @throws IllegalArgumentException when position is out of bounds.
     * @see #colorAt(Offset)
     */
    public Color colorAt(final int x, final int y) {
        final Image image = image();
        if (x < 0 || x >= image.getWidth(null) || y < 0 || y >= image.getHeight(null)) {
            throw new IllegalArgumentException(format("position is out of bounds: %d:%d", x, y));
        }
        final int rgb = ImageOperations.toBufferedImage(image).getRGB(x, y);
        final java.awt.Color awtColor = new java.awt.Color(rgb, true);
        final Percent opacity = Percent.of(awtColor.getAlpha() / 255.0);
        return Color.rgb(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue(), opacity);
    }

    /**
     * Returns a new instance. The new {@link Frame}s old {@link Color} is replaced
     * with a new one. This method is quite slow.
     */
    public Frame replaceColor(final Color oldColor, final Color newColor) {
        final Image oldImage = imageStorage.getImage();
        final Image newImage = ImageOperations.applyFilter(oldImage, new ReplaceColorFilter(oldColor, newColor));
        return new Frame(newImage, duration);
    }

    /**
     * Returns a scaled version of the current {@link Frame}.
     */
    public Frame scaled(final double scale) {
        final int width = (int) (width() * scale);
        final int height = (int) (height() * scale);
        Validate.positive(width, "scaled image width is invalid");
        Validate.positive(height, "scaled image height is invalid");
        final var newImage = image().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new Frame(newImage, duration);
    }

    /**
     * Returns a new instance of a {@link Frame} without transparent pixels on the left and the right side.
     */
    public Frame cropHorizontal() {
        int minX = 0;
        int maxX = size().width() - 1;

        while (minX < maxX && hasOnlyTransparentPixelInColumn(minX)) {
            minX++;
        }

        while (maxX > minX && hasOnlyTransparentPixelInColumn(maxX)) {
            maxX--;
        }

        final var offset = Offset.at(minX, 0);
        final var target = Offset.at(maxX + 1, height());
        final var size = Size.definedBy(offset, target);
        return extractArea(offset, size);
    }

    /**
     * Returns a list of all pixels that have a different color than the other {@link Frame}.
     */
    public List<Offset> listPixelDifferences(final Frame other) {
        if (!size().equals(other.size())) {
            throw new IllegalArgumentException("other frame must have identical size to compare pixels");
        }
        final var distinct = new ArrayList<Offset>();
        for (final var offset : size().allPixels()) {
            if (!colorAt(offset).equals(other.colorAt(offset))) {
                distinct.add(offset);
            }
        }
        return distinct;
    }

    /**
     * Lists all distinct {@link Color colors} contained in this {@link Frame}.
     */
    public Set<Color> colors() {
        final Set<Color> colors = new HashSet<>();
        for (final var offset : size().allPixels()) {
            colors.add(colorAt(offset));
        }
        return colors;
    }

    /**
     * Returns a new {@link Frame} with border of specified width and color.
     *
     * @since 2.15.0
     */
    public Frame addBorder(final int width, final Color color) {
        final var newImage = ImageOperations.addBorder(image(), width, color);
        return new Frame(newImage, duration);
    }

    /**
     * Exports the frame as png file at the specified location.
     *
     * @since 2.15.0
     */
    public void exportPng(final String fileName) {
        requireNonNull(fileName, "file name must not be null");
        final String exportName = fileName.endsWith(".png") ? fileName : fileName + ".png";
        try {
            ImageIO.write(ImageOperations.toBufferedImage(image()), "png", new File(exportName));
        } catch (IOException e) {
            throw new IllegalStateException("could not export frame as png file: " + fileName, e);
        }
    }

    /**
     * Renders all shader images to increase game performance.
     * Should be called from {@link Scene#populate(Environment)}
     *
     * @see #compileShader(Shader)
     * @since 2.15.0
     */
    public void compileShader(final Supplier<ShaderSetup> shaderSetup) {
        compileShader(shaderSetup.get().shader());
    }

    /**
     * Renders all shader images to increase game performance.
     * Should be called from {@link Scene#populate(Environment)}
     *
     * @see #compileShader(Supplier)
     * @since 2.15.0
     */
    public void compileShader(final Shader shader) {
        final int preparations = shader.isAnimated() ? (SHADER_CACHE_LIMIT - 1) : 0;
        for (int i = 0; i <= preparations; i++) {
            final String cacheKey = shader.isAnimated()
                    ? shader.cacheKey() + i
                    : shader.cacheKey();
            final var progress = Percent.of(i / (1.0 * (SHADER_CACHE_LIMIT - 1)));
            shaderCache.getOrElse(cacheKey, () -> shader.apply(image(), progress));
        }
    }

    /**
     * Clears all entries from the shader cache.
     *
     * @see #shaderCacheSize()
     * @since 2.15.0
     */
    public void clearShaderCache() {
        shaderCache.clear();
    }

    /**
     * Returns the number of entries in the shader cache.
     *
     * @see #clearShaderCache()
     * @since 2.15.0
     */
    public int shaderCacheSize() {
        return shaderCache.size();
    }

    /**
     * Returns the image of the {@link Frame} after applying {@link ShaderSetup} on the image.
     *
     * @param shaderSetup {@link ShaderSetup} used. is nullable
     * @param time        current time used to get the right image from animated shaders.
     * @since 2.15.0
     */
    public Image image(final ShaderSetup shaderSetup, final Time time) {
        if (isNull(shaderSetup)) {
            return image();
        }
        final long totalNanos = shaderSetup.duration().nanos();
        final var progress = isNull(shaderSetup.progress())
                ? Percent.of(((time.nanos() - shaderSetup.offset().nanos()) % totalNanos) / (1.0 * totalNanos))
                : shaderSetup.progress();
        final var easedProgress = shaderSetup.ease().applyOn(progress);
        final String cacheKey = calculateCacheKey(shaderSetup.shader(), easedProgress);
        return shaderCache.getOrElse(cacheKey, () -> shaderSetup.shader().apply(image(), easedProgress));
    }

    /**
     * Returns the distinct {@link Color colors} used in this {@link Frame}.
     *
     * @since 2.18.0
     */
    public Set<Color> colorPalette() {
        return size().allPixels().stream().map(this::colorAt).collect(Collectors.toSet());
    }

    private String calculateCacheKey(final Shader shader, final Percent progress) {
        return shader.isAnimated()
                ? shader.cacheKey() + (int) (progress.value() * SHADER_CACHE_LIMIT)
                : shader.cacheKey();
    }

    private boolean hasOnlyTransparentPixelInColumn(final int x) {
        for (int y = 0; y < height(); y++) {
            if (!colorAt(x, y).equals(Color.TRANSPARENT)) {
                return false;
            }
        }
        return true;
    }
}