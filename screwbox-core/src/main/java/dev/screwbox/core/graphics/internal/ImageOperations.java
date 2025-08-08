package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.util.Objects;

public final class ImageOperations {

    private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();

    private ImageOperations() {
    }

    public static BufferedImage toBufferedImage(final Image image) {
        return image instanceof final BufferedImage bufferImage
                ? bufferImage
                : cloneImage(image);
    }

    public static BufferedImage applyFilter(final Image image, final ImageFilter filter) {
        final ImageProducer imageProducer = new FilteredImageSource(image.getSource(), filter);
        final Image newImage = TOOLKIT.createImage(imageProducer);
        return toBufferedImage(newImage); // convert to BufferedImage to avoid flickering
    }

    public static Image addBorder(final Image image, final int width, final Color color) {
        Validate.positive(width, "width must be positive");
        Objects.requireNonNull(color, "color must not be null");
        final int resultWidth = image.getWidth(null) + width * 2;
        final int resultHeight = image.getHeight(null) + width * 2;
        final var newImage = createImage(Size.of(resultWidth, resultHeight));
        final var graphics = newImage.getGraphics();
        if (!color.opacity().isZero()) {
            graphics.setColor(AwtMapper.toAwtColor(color));
            graphics.fillRect(0, 0, resultWidth, resultHeight);
        }
        graphics.drawImage(image, width, width, null);
        graphics.dispose();
        return newImage;
    }

    public static BufferedImage createImage(final Size size) {
        return new BufferedImage(size.width(), size.height(), BufferedImage.TYPE_INT_ARGB);
    }

    public static BufferedImage createEmptyImageOfSameSize(final Image source) {
        return createImage(Size.of(source.getWidth(null), source.getHeight(null)));
    }

    public static BufferedImage cloneImage(final Image source) {
        final var clone = createEmptyImageOfSameSize(source);
        final var graphics = clone.getGraphics();
        graphics.drawImage(source, 0, 0, null);
        graphics.dispose();
        return clone;
    }

    /**
     * Copies top {@link Image} on top of bottom {@link Image}. Images must have same size.
     *
     * @since 3.7.0
     */
    public static Image stackImages(final Image bottom, final Image top) {
        Validate.isTrue(() -> bottom.getHeight(null) == top.getHeight(null) && bottom.getWidth(null) == top.getWidth(null), "images must have same size");
        final var result = ImageOperations.cloneImage(bottom);
        var graphics = (Graphics2D) result.getGraphics();
        graphics.drawImage(top, 0, 0, null);
        graphics.dispose();
        return result;
    }
}
