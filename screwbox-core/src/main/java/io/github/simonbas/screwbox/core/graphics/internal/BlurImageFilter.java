package io.github.simonbas.screwbox.core.graphics.internal;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Arrays;
import java.util.function.UnaryOperator;

public class BlurImageFilter implements UnaryOperator<BufferedImage> {

    private final ConvolveOp convolveOperation;

    public BlurImageFilter(final int radius) {
        final float weight = 1f / (radius * radius);
        final float[] data = new float[radius * radius];

        Arrays.fill(data, weight);

        final Kernel kernel = new Kernel(radius, radius, data);
        convolveOperation = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    }

    @Override
    public BufferedImage apply(final BufferedImage source) {
        return convolveOperation.filter(source, null);
    }

}
