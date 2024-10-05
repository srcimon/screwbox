package io.github.srcimon.screwbox.core.graphics.internal.filter;

import java.awt.image.BufferedImage;

public class BlurImageFilter extends BlurImageAndIncreaseSizeFilter {

    public BlurImageFilter(final int radius) {
        super(radius);
    }

    @Override
    public BufferedImage apply(final BufferedImage image) {
        final BufferedImage blurred = super.apply(image);

        // return just center
        return blurred.getSubimage(radius, radius, image.getWidth(), image.getHeight());
    }
}