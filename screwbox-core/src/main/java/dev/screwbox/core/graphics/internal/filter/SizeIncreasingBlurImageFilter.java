package dev.screwbox.core.graphics.internal.filter;

import java.awt.image.BufferedImage;

//TODO stack size increase the other way around
public class SizeIncreasingBlurImageFilter extends SizeIncreasingImageFilter {

    public SizeIncreasingBlurImageFilter(final int radius) {
        super(radius);
    }

    @Override
    public BufferedImage apply(final BufferedImage image) {
        return super.apply(new BlurImageFilter(radius).apply(image));
    }
}
