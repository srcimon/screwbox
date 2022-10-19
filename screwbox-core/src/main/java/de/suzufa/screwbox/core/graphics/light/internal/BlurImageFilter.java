package de.suzufa.screwbox.core.graphics.light.internal;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.function.Function;

public class BlurImageFilter implements Function<BufferedImage, BufferedImage> {

    private final int radius;

    public BlurImageFilter(final int radius) {
        this.radius = radius;
    }

    // TODO: cutoff edges to avoid shit

    @Override
    public BufferedImage apply(final BufferedImage source) {
        final int size = radius * 2 + 1;
        final float weight = 1.0f / (size * size);
        final float[] data = new float[size * size];

        for (int i = 0; i < data.length; i++) {
            data[i] = weight;
        }

        final Kernel kernel = new Kernel(size, size, data);
        final ConvolveOp convolveOperation = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);// TODO: cutoff edges
                                                                                                 // to avoid shit
        return convolveOperation.filter(source, null);
    }

}
