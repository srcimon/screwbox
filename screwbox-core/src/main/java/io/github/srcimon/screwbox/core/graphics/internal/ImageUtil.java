package io.github.srcimon.screwbox.core.graphics.internal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;

public final class ImageUtil {

    private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();

    private ImageUtil() {
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
