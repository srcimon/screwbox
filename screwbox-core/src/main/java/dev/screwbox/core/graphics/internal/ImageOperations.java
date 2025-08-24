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
    private static final GraphicsConfiguration GRAPHICS_CONFIGURATION = GraphicsEnvironment.isHeadless() ? null
            : GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

    private ImageOperations() {
    }

    public static BufferedImage toBufferedImage(final Image image) {
        final BufferedImage bufferedImage = createImage(image.getWidth(null), image.getHeight(null));
        Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return bufferedImage;
    }

    public static BufferedImage applyFilter(final Image image, final ImageFilter filter) {
        final ImageProducer imageProducer = new FilteredImageSource(image.getSource(), filter);
        final Image newImage = TOOLKIT.createImage(imageProducer);
        return toBufferedImage(newImage);
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

    public static BufferedImage createImage(final int width, int height) {
        return createImage(Size.of(width, height));
    }

    public static BufferedImage createImage(final Size size) {
        return GRAPHICS_CONFIGURATION.createCompatibleImage(size.width(), size.height(), Transparency.TRANSLUCENT);
    }

    public static BufferedImage createEmptyImageOfSameSize(final Image source) {
        return createImage(source.getWidth(null), source.getHeight(null));
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

    //TODO GH Issue #709
}
