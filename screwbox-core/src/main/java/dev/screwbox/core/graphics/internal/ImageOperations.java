package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.util.Objects;

import static java.util.Objects.isNull;

public final class ImageOperations {

    private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();
    private static final GraphicsConfiguration GRAPHICS_CONFIGURATION = GraphicsEnvironment.isHeadless()
        ? null
        : GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

    private ImageOperations() {
    }

    public static BufferedImage applyFilter(final Image image, final ImageFilter filter, Size size) {
        final ImageProducer imageProducer = new FilteredImageSource(image.getSource(), filter);
        final Image filteredImage = TOOLKIT.createImage(imageProducer);
        return cloneImage(filteredImage, size);
    }

    public static BufferedImage applyFilter(final Image image, final ImageFilter filter) {
        final ImageProducer imageProducer = new FilteredImageSource(image.getSource(), filter);
        final Image filteredImage = TOOLKIT.createImage(imageProducer);
        return cloneImage(filteredImage);
    }

    public static Image addBorder(final Image image, final int width, final Color color) {
        Validate.positive(width, "width must be positive");
        Objects.requireNonNull(color, "color must not be null");
        final int resultWidth = image.getWidth(null) + width * 2;
        final int resultHeight = image.getHeight(null) + width * 2;
        final var newImage = createImage(resultWidth, resultHeight);
        final var graphics = newImage.getGraphics();
        if (color.isVisible()) {
            graphics.setColor(AwtMapper.toAwtColor(color));
            graphics.fillRect(0, 0, resultWidth, resultHeight);
        }
        graphics.drawImage(image, width, width, null);
        graphics.dispose();
        return newImage;
    }

    // must be synchronized because image API is not thread save!
    public static synchronized BufferedImage createImage(final int width, final int height) {
        return isNull(GRAPHICS_CONFIGURATION)
            ? new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
            : GRAPHICS_CONFIGURATION.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }

    public static BufferedImage createImage(final Size size) {
        return createImage(size.width(), size.height());
    }

    public static BufferedImage createEmptyImageOfSameSize(final Image source) {
        return createImage(source.getWidth(null), source.getHeight(null));
    }

    public static BufferedImage toBufferedImage(final Image image) {
        return image instanceof BufferedImage bufferedImage
            ? bufferedImage
            : cloneImage(image);
    }

    public static BufferedImage cloneImage(final Image source, Size size) {
        final var clone = createImage(size);
        final var graphics = clone.getGraphics();
        graphics.drawImage(source, 0, 0, null);
        graphics.dispose();
        return clone;
    }

    public static BufferedImage cloneImage(final Image source) {
        final var clone = createEmptyImageOfSameSize(source);
        final var graphics = clone.getGraphics();
        graphics.drawImage(source, 0, 0, null);
        graphics.dispose();
        return clone;
    }

    public static Image stackImages(final Image bottom, final Image top) {
        Validate.isTrue(() -> bottom.getHeight(null) == top.getHeight(null) && bottom.getWidth(null) == top.getWidth(null), "images must have same size");
        final var result = ImageOperations.cloneImage(bottom);
        var graphics = (Graphics2D) result.getGraphics();
        graphics.drawImage(top, 0, 0, null);
        graphics.dispose();
        return result;
    }

    public static void invertOpacity(final BufferedImage image) {
        final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        final int numPixels = pixels.length;
        for (int i = 0; i < numPixels; i++) {
            final int currentPixel = pixels[i];
            final int invertedAlpha = 255 - ((currentPixel >> 24) & 0xFF);
            final int rgbChannels = currentPixel & 0x00FFFFFF;
            pixels[i] = (invertedAlpha << 24) | rgbChannels;
        }
    }
}
