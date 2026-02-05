package dev.screwbox.core.graphics.internal.filter;

import dev.screwbox.core.graphics.internal.ImageOperations;

import java.awt.image.BufferedImage;

public class BlurImageFilter {

    private final int radius;

    public BlurImageFilter(final int radius) {
        this.radius = radius;
    }

    public BufferedImage apply(final BufferedImage image) {
        var clone = ImageOperations.cloneImage(image);
        ImageOperations.blurImage(clone, radius);
        return clone;
    }
}