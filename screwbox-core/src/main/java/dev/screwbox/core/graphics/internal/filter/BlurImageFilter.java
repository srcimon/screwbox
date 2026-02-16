package dev.screwbox.core.graphics.internal.filter;

import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.utils.Validate;

import java.awt.image.BufferedImage;

public class BlurImageFilter {

    private final int radius;

    public BlurImageFilter(final int radius) {
        Validate.range(radius, 1, 20, "radius must be in range 1 to 20");
        this.radius = radius;
    }

    public BufferedImage apply(final BufferedImage image) {
        final var clone = ImageOperations.cloneImage(image);
        ImageOperations.blurImage(clone, radius);
        return ImageOperations.cloneImage(clone); // re-enable gpu usage
    }
}