package io.github.srcimon.screwbox.core.graphics.internal.filter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.UnaryOperator;

public class SizeIncreasingImageFilter implements UnaryOperator<BufferedImage> {

    protected final int radius;

    public SizeIncreasingImageFilter(final int radius) {
        if (radius < 1 || radius > 6) {
            throw new IllegalArgumentException("radius must be in range 1 to 6");
        }
        this.radius = radius;
    }

    @Override
    public BufferedImage apply(final BufferedImage image) {
        final BufferedImage newImage = new BufferedImage(image.getWidth() + radius * 2, image.getHeight() + radius * 2, BufferedImage.TYPE_INT_ARGB);
        final var graphics = (Graphics2D) newImage.getGraphics();

        // draw image scaled in the corners
        graphics.setClip(new Rectangle(0, 0, radius * 2 + image.getWidth(), radius));
        graphics.drawImage(image, 0, 0, image.getWidth() + radius * 2, image.getHeight() + radius * 2, null);

        graphics.setClip(new Rectangle(0, image.getHeight() + radius, radius * 2 + image.getWidth(), radius));
        graphics.drawImage(image, 0, 0, image.getWidth() + radius * 2, image.getHeight() + radius * 2, null);

        graphics.setClip(new Rectangle(0, radius, radius, image.getHeight()));
        graphics.drawImage(image, 0, 0, image.getWidth() + radius * 2, image.getHeight() + radius * 2, null);

        graphics.setClip(new Rectangle(radius + image.getWidth(), radius, radius, image.getHeight()));
        graphics.drawImage(image, 0, 0, image.getWidth() + radius * 2, image.getHeight() + radius * 2, null);

        // draw image in correct size
        graphics.setClip(new Rectangle(radius, radius, image.getWidth(), image.getHeight()));
        graphics.drawImage(image, radius, radius, image.getWidth(), image.getHeight(), null);
        graphics.dispose();

        return newImage;
    }
}
