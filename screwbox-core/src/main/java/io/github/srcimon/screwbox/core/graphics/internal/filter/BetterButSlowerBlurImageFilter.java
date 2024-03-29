package io.github.srcimon.screwbox.core.graphics.internal.filter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.UnaryOperator;

public class BetterButSlowerBlurImageFilter implements UnaryOperator<BufferedImage> {

    private final int radius;

    public BetterButSlowerBlurImageFilter(int radius) {
        if (radius < 1 || radius > 8) {
            throw new IllegalArgumentException("radius must be in range 1 to 8");
        }
        this.radius = radius;
    }

    @Override
    public BufferedImage apply(final BufferedImage image) {
        var newImage = new BufferedImage(image.getWidth()+radius *2, image.getHeight() + radius * 2, image.getType());
        var graphics = (Graphics2D)newImage.getGraphics();
        graphics.drawImage(image, 0, 0, image.getWidth() + radius * 2, image.getHeight() + radius * 2, null);
        final BufferedImage blurred = new BlurImageFilter(radius).apply(newImage);
        return blurred.getSubimage(radius, radius, image.getWidth(), image.getHeight());
    }

}
