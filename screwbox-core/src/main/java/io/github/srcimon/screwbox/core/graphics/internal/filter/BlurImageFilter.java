package io.github.srcimon.screwbox.core.graphics.internal.filter;

import java.awt.image.BufferedImage;

public class BlurImageFilter extends SizeIncreasingBlurImageFilter {

    public BlurImageFilter(final int radius) {
        super(radius);
    }

    @Override
    public BufferedImage apply(final BufferedImage image) {
        var blurred = super.apply(image);

        // return just center to remove size increase
        return blurred.getSubimage(radius, radius, image.getWidth(), image.getHeight());
    }
}