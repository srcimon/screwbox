package io.github.srcimon.screwbox.core.graphics.internal.filter;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.internal.AwtMapper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Arrays;
import java.util.function.UnaryOperator;

public class BlurImageFilter implements UnaryOperator<BufferedImage> {

    private final int radius;
    private final ConvolveOp convolveOperation;

    public BlurImageFilter(final int radius) {
        if (radius < 1 || radius > 6) {
            throw new IllegalArgumentException("radius must be in range 1 to 6");
        }
        this.radius = radius + 1;
        final float weight = 1f / (radius * radius);
        final float[] data = new float[radius * radius];

        Arrays.fill(data, weight);

        final Kernel kernel = new Kernel(radius, radius, data);
        convolveOperation = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    }

    @Override
    public BufferedImage apply(final BufferedImage image) {
        final BufferedImage newImage = new BufferedImage(image.getWidth() + radius * 2, image.getHeight() + radius * 2, BufferedImage.TYPE_INT_ARGB);
        final var graphics = (Graphics2D) newImage.getGraphics();

        // draw image scaled
        graphics.drawImage(image, 0, 0, image.getWidth() + radius * 2, image.getHeight() + radius * 2, null);

        // clear center
        graphics.setBackground(AwtMapper.toAwtColor(Color.TRANSPARENT));
        graphics.clearRect(radius, radius, image.getWidth(), image.getHeight());

        // draw image in correct size
        graphics.drawImage(image, radius, radius, image.getWidth(), image.getHeight(), null);
        graphics.dispose();

        // blur (leaves outer radius untouched)
        final BufferedImage blurred = convolveOperation.filter(newImage, null);

        // return just center
        return blurred.getSubimage(radius, radius, image.getWidth(), image.getHeight());
    }


}

