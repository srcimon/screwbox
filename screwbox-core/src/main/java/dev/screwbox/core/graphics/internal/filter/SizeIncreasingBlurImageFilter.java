package dev.screwbox.core.graphics.internal.filter;

import dev.screwbox.core.graphics.internal.ImageOperations;

import java.awt.image.BufferedImage;

//TODO stack size increase the other way around
public class SizeIncreasingBlurImageFilter extends SizeIncreasingImageFilter {

    public SizeIncreasingBlurImageFilter(final int radius) {
        super(radius);
    }

    @Override
    public BufferedImage apply(final BufferedImage image) {
        return super.apply(blur(image, radius));
    }

    private BufferedImage blur(final BufferedImage image, final int radius) {
        ImageOperations.applyBlur(image, radius);
        return image;
    }
}
