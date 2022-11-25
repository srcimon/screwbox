package de.suzufa.screwbox.core.graphics.internal;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

import de.suzufa.screwbox.core.graphics.Color;

//TODO: inline ImageUtil in Frame
//TODO: MANY OPTIMIZATIONS IN THIS CLASS => MAKING BUFFER IAMGE TO IMAGE NOT NECESSARY
public final class ImageUtil {

    private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();

    private ImageUtil() {
    }

    // TODO: is specialized version of replaceColor
    public static Image makeColorTransparent(final Image image, final Color transparencyColor) {
        // TODO: fix transparency color not used
        final ImageFilter filter = new RGBImageFilter() {

            // the color we are looking for... Alpha bits are set to opaque

            int markerRGB = AwtMapper.toAwtColor(transparencyColor).getRGB() | 0xFF000000;

            @Override
            public final int filterRGB(final int x, final int y, final int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    // nothing to do
                    return rgb;
                }
            }
        };

        final ImageProducer imageProducer = new FilteredImageSource(image.getSource(), filter);
        return TOOLKIT.createImage(imageProducer);
    }

    // TODO: simplyfiy and test

    public static Image scale(final Image image, final double scale) {
        final int width = (int) (image.getWidth(null) * scale);
        final int height = (int) (image.getHeight(null) * scale);
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Scaled image is size is invalid");
        }
        return image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
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
