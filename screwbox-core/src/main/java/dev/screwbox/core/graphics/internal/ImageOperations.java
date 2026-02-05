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

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE;
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
            ? new BufferedImage(width, height, TYPE_INT_ARGB)
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
        var graphics = result.createGraphics();
        graphics.drawImage(top, 0, 0, null);
        graphics.dispose();
        return result;
    }

    /**
     * Inverts opacity of {@link BufferedImage}. Supports only {@link BufferedImage#TYPE_INT_ARGB} and {@link BufferedImage#TYPE_INT_ARGB_PRE} at the moment.
     */
    public static void invertOpacity(final BufferedImage image) {
        Validate.isTrue(() -> image.getType() == TYPE_INT_ARGB_PRE || image.getType() == TYPE_INT_ARGB, "image type not supported: " + image.getType());
        final int[] pixels = getPixelValues(image);
        final int numPixels = pixels.length;
        for (int i = 0; i < numPixels; i++) {
            final int currentPixel = pixels[i];
            final int invertedAlpha = 255 - ((currentPixel >> 24) & 0xFF);
            final int rgbChannels = currentPixel & 0x00FFFFFF;
            pixels[i] = (invertedAlpha << 24) | rgbChannels;
        }
    }

    /**
     * Blurs the specified image.
     *
     * @since 3.22.0
     */
    public static void blurImage(final BufferedImage enlarged, final int radius) {
        Validate.range(radius, 1, 20, "radius must be in range 1 to 20");
        final int[] pixels = getPixelValues(enlarged);
        final int[] temp = new int[pixels.length];
        blurPassHorizontal(pixels, temp, enlarged.getWidth(), enlarged.getHeight(), radius);
        blurPassVertical(temp, pixels, enlarged.getHeight(), enlarged.getWidth(), radius);
    }

    private static void blurPassHorizontal(final int[] in, final int[] out, final int width, final int height, final int radius) {
        final float scale = 1.0f / (radius * 2 + 1);

        for (int y = 0; y < height; y++) {
            float sumRed = 0;
            float sumGreen = 0;
            float sumBlue = 0;
            float sumAlpha = 0;

            for (int i = -radius; i <= radius; i++) {
                int index = in[y * width + Math.max(0, Math.min(width - 1, i))];
                sumAlpha += Color.alphaFromRgb(index);
                sumRed += Color.redFromRgb(index);
                sumGreen += Color.greenFromRgb(index);
                sumBlue += Color.blueFromRgb(index);
            }

            for (int x = 0; x < width; x++) {
                out[y * width + x] = ((int) (sumAlpha * scale) << 24) | ((int) (sumRed * scale) << 16) | ((int) (sumGreen * scale) << 8) | (int) (sumBlue * scale);

                int p1 = in[y * width + Math.min(width - 1, x + radius + 1)];
                int p2 = in[y * width + Math.max(0, x - radius)];

                sumAlpha += Color.alphaFromRgb(p1) - Color.alphaFromRgb(p2);
                sumRed += Color.redFromRgb(p1) - Color.redFromRgb(p2);
                sumGreen += Color.greenFromRgb(p1) - Color.greenFromRgb(p2);
                sumBlue += Color.blueFromRgb(p1) - Color.blueFromRgb(p2);
            }
        }
    }

    private static void blurPassVertical(final int[] in, final int[] out, final int width, final int height, final int radius) {
        final float scale = 1.0f / (radius * 2 + 1);

        for (int y = 0; y < height; y++) {
            float sumRed = 0;
            float sumGreen = 0;
            float sumBlue = 0;
            float sumAlpha = 0;

            for (int i = -radius; i <= radius; i++) {
                int p = in[y + Math.max(0, Math.min(width - 1, i)) * height];
                sumAlpha += Color.alphaFromRgb(p);
                sumRed += Color.redFromRgb(p);
                sumGreen += Color.greenFromRgb(p);
                sumBlue += Color.blueFromRgb(p);
            }

            for (int x = 0; x < width; x++) {
                out[y + x * height] = ((int) (sumAlpha * scale) << 24) | ((int) (sumRed * scale) << 16) | ((int) (sumGreen * scale) << 8) | (int) (sumBlue * scale);

                int p1 = in[y + Math.min(width - 1, x + radius + 1) * height];
                int p2 = in[y + Math.max(0, x - radius) * height];

                sumAlpha += Color.alphaFromRgb(p1) - Color.alphaFromRgb(p2);
                sumRed += Color.redFromRgb(p1) - Color.redFromRgb(p2);
                sumGreen += Color.greenFromRgb(p1) - Color.greenFromRgb(p2);
                sumBlue += Color.blueFromRgb(p1) - Color.blueFromRgb(p2);
            }
        }
    }

    private static int[] getPixelValues(final BufferedImage image) {
        return ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }
}
