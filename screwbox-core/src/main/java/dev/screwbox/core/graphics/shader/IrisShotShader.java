package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;
import java.awt.image.RGBImageFilter;
import java.io.Serial;

public class IrisShotShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

    public IrisShotShader() {
        super("iris-shot");
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final var sourceImage = ImageOperations.cloneImage(source);
        final int centerX = sourceImage.getWidth() / 2;
        final int centerY = sourceImage.getHeight() / 2;
        double maxDistanceToCenter = Math.sqrt(1.0 * centerX * centerX + centerY * centerY);

        final var filter = new RGBImageFilter() {

            @Override
            public int filterRGB(int x, int y, int rgb) {
                final int distanceX = Math.abs(centerX - x);
                final int distanceY = Math.abs(centerY - y);
                final double distanceToCenter = Math.sqrt(1.0 * distanceX * distanceX + distanceY * distanceY);
                boolean isOutOfDeadZone = distanceToCenter + 1 <= maxDistanceToCenter * progress.invert().value();
                return isOutOfDeadZone ? sourceImage.getRGB(x, y) : 0;
            }
        };
        return ImageOperations.applyFilter(sourceImage, filter);
    }
}
