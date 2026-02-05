package dev.screwbox.core.graphics.internal.filter;

import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.utils.Validate;

import java.awt.image.BufferedImage;
import java.util.function.UnaryOperator;

public class ExpandImageFilter implements UnaryOperator<BufferedImage> {

    protected final int radius;

    public ExpandImageFilter(final int radius) {
        Validate.range(radius, 1, 6, "radius must be in range 1 to 6");
        this.radius = radius;
    }

    @Override
    public BufferedImage apply(final BufferedImage image) {
        final BufferedImage newImage = ImageOperations.createImage(image.getWidth() + radius * 2, image.getHeight() + radius * 2);
        final var graphics = newImage.createGraphics();

        // draw image scaled in the corners
        graphics.setClip(0, 0, radius * 2 + image.getWidth(), radius);
        graphics.drawImage(image, 0, 0, image.getWidth() + radius * 2, image.getHeight() + radius * 2, null);

        graphics.setClip(0, image.getHeight() + radius, radius * 2 + image.getWidth(), radius);
        graphics.drawImage(image, 0, 0, image.getWidth() + radius * 2, image.getHeight() + radius * 2, null);

        graphics.setClip(0, radius, radius, image.getHeight());
        graphics.drawImage(image, 0, 0, image.getWidth() + radius * 2, image.getHeight() + radius * 2, null);

        graphics.setClip(radius + image.getWidth(), radius, radius, image.getHeight());
        graphics.drawImage(image, 0, 0, image.getWidth() + radius * 2, image.getHeight() + radius * 2, null);

        // draw image in correct size
        graphics.setClip(radius, radius, image.getWidth(), image.getHeight());
        graphics.drawImage(image, radius, radius, image.getWidth(), image.getHeight(), null);
        graphics.dispose();

        return newImage;
    }
}
