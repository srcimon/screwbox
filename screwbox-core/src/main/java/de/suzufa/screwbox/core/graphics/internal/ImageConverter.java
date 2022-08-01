package de.suzufa.screwbox.core.graphics.internal;

import static java.lang.String.format;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

import de.suzufa.screwbox.core.graphics.Color;

//TODO: Make ImageConverter part of Frame
//TODO: MANY OPTIMIZATIONS IN THIS CLASS => MAKING BUFFER IAMGE TO IMAGE NOT NECESSARY
public final class ImageConverter {

    private ImageConverter() {
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
        return Toolkit.getDefaultToolkit().createImage(imageProducer);
    }

    public static Image replaceColor(final Image image, final Color oldColor, final Color newColor) {
        final ImageFilter filter = new RGBImageFilter() {

            // the color we are looking for... Alpha bits are set to opaque

            int oldColorAwt = AwtMapper.toAwtColor(oldColor).getRGB();
            int newColorAwt = AwtMapper.toAwtColor(newColor).getRGB();

            @Override
            public final int filterRGB(final int x, final int y, final int rgb) {
                if (rgb == oldColorAwt) {
                    return newColorAwt;
                } else {
                    return rgb;
                }
            }
        };

        final ImageProducer imageProducer = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(imageProducer);
    }

    // TODO: simplyfiy and test
    public static Image flipHorizontally(final Image image) {
        final BufferedImage bufferedImage = toBufferedImage(image);

        final AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        final AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        final BufferedImage result = op.filter(bufferedImage, null);

        // to image
        final ImageProducer imageProducer = new FilteredImageSource(result.getSource(), new RGBImageFilter() {

            // the color we are looking for... Alpha bits are set to opaque

            @Override
            public final int filterRGB(final int x, final int y, final int rgb) {
                return rgb;
            }
        });
        return Toolkit.getDefaultToolkit().createImage(imageProducer);

    }

    public static Image flipVertically(final Image image) {
        final BufferedImage bufferedImage = toBufferedImage(image);

        final AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -image.getHeight(null));
        final AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        final BufferedImage result = op.filter(bufferedImage, null);

        // to image
        final ImageProducer imageProducer = new FilteredImageSource(result.getSource(), new RGBImageFilter() {

            // the color we are looking for... Alpha bits are set to opaque

            @Override
            public final int filterRGB(final int x, final int y, final int rgb) {
                return rgb;
            }
        });
        return Toolkit.getDefaultToolkit().createImage(imageProducer);
    }

    public static Color colorAt(final Image image, final int x, final int y) {
        if (x < 0 || x > image.getWidth(null) || y < 0 || y > image.getHeight(null)) {
            throw new IllegalArgumentException(format("Dimension is out of bounds: %d:%d", x, y));
        }
        final BufferedImage bufferedImage = toBufferedImage(image);
        final int rgb = bufferedImage.getRGB(x, y);
        final java.awt.Color awtColor = new java.awt.Color(rgb, true);
        return AwtMapper.toColor(awtColor);
    }

    private static BufferedImage toBufferedImage(final Image image) {
        final int width = image.getWidth(null);
        final int height = image.getHeight(null);
        final var bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return bufferedImage;
    }

}
