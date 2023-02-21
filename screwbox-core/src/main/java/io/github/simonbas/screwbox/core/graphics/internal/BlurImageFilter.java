package io.github.simonbas.screwbox.core.graphics.internal;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.function.UnaryOperator;

public class BlurImageFilter implements UnaryOperator<BufferedImage> {

    private final ConvolveOp convolveOperation;

    public BlurImageFilter(final int radius) {
        final float weight = 1f / (radius * radius);
        final float[] data = new float[radius * radius];

        for (int i = 0; i < data.length; i++) {
            data[i] = weight;
        }

        final Kernel kernel = new Kernel(radius, radius, data);
        convolveOperation = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    }

    @Override
    public BufferedImage apply(final BufferedImage source) {
        return convolveOperation.filter(source, null);
    }

}
