package io.github.srcimon.screwbox.core.graphics.internal.filter;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Arrays;

public class SizeIncreasingBlurImageFilter extends SizeIncreasingImageFilter {

    private final ConvolveOp convolveOperation;

    public SizeIncreasingBlurImageFilter(final int radius) {
        super(radius);
        final float weight = 1f / (radius * radius);
        final float[] data = new float[radius * radius];

        Arrays.fill(data, weight);

        final Kernel kernel = new Kernel(radius, radius, data);
        convolveOperation = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    }

    @Override
    public BufferedImage apply(final BufferedImage image) {
        final var newImage = super.apply(image);

        // blur (leaves outer radius untouched)
        return convolveOperation.filter(newImage, null);
    }
}
