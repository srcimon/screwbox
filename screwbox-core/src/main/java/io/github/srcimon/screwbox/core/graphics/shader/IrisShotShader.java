package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;
import java.awt.image.RGBImageFilter;
import java.io.Serial;

import static io.github.srcimon.screwbox.core.graphics.internal.ImageOperations.toBufferedImage;

public class IrisShotShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

    public IrisShotShader() {
        super("iris-shot");
    }

    @Override
    public Image apply(Image source, Percent progress) {
        final var sourceImage = toBufferedImage(source);
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
