package io.github.simonbas.screwbox.core.graphics.internal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;

public final class ImageUtil {

    private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();

    private ImageUtil() {
    }

    // TODO: simplyfiy and test
    public static Image scale(final Image image, final double scale) {
        final int width = (int) (image.getWidth(null) * scale);
        final int height = (int) (image.getHeight(null) * scale);
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Scaled image is size is invalid");
        }
        return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public static BufferedImage toBufferedImage(final Image image) {
        if (image instanceof final BufferedImage bufferImage) {
            return bufferImage;
        }
        final int width = image.getWidth(null);
        final int height = image.getHeight(null);
        final var bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return bufferedImage;
    }

    public static BufferedImage applyFilter(final Image image, final ImageFilter filter) {
        final ImageProducer imageProducer = new FilteredImageSource(image.getSource(), filter);
        final Image newImage = TOOLKIT.createImage(imageProducer);
        return toBufferedImage(newImage); // convert to BufferedImage to avoid flickering
    }

}
