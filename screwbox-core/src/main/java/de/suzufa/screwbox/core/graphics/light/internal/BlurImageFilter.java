package de.suzufa.screwbox.core.graphics.light.internal;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.function.Function;

public class BlurImageFilter implements Function<BufferedImage, BufferedImage> {

    private final ConvolveOp convolveOperation;

    public BlurImageFilter(final int radius) {
        final float weight = 1.0f / (radius * radius);
        final float[] data = new float[radius * radius];

        for (int i = 0; i < data.length; i++) {
            data[i] = weight;
        }

        final Kernel kernel = new Kernel(radius, radius, data);
        convolveOperation = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);// TODO: cutoff edges
                                                                                // to avoid shit
    }

    // TODO: cutoff edges to avoid shit

    @Override
    public BufferedImage apply(final BufferedImage source) {
        return convolveOperation.filter(source, null);
    }

}
