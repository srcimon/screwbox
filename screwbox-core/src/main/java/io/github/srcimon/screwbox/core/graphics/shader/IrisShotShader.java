package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;
import java.awt.image.RGBImageFilter;

import static io.github.srcimon.screwbox.core.graphics.internal.ImageOperations.toBufferedImage;

public class IrisShotShader extends Shader {

    public IrisShotShader() {
        super("iris-shot");
    }

    @Override
    public Image apply(Image source, Percent progress) {
        final var sourceImage = toBufferedImage(source);
        final int centerX = sourceImage.getWidth() / 2;
        final int centerY = sourceImage.getHeight() / 2;
        double maxDistanceToCenter = Math.sqrt(centerX * centerX + centerY * centerY);

        final var filter = new RGBImageFilter() {

            @Override
            public int filterRGB(int x, int y, int rgb) {
                final int distanceX = Math.abs(centerX - x);
                final int distanceY = Math.abs(centerY - y);
                final double distanceToCenter = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
                boolean isOutOfDeadZone = distanceToCenter + 1 <= maxDistanceToCenter * progress.invert().value();
                return isOutOfDeadZone ? sourceImage.getRGB(x, y) : 0;
            }
        };
        return ImageOperations.applyFilter(sourceImage, filter);
    }
}
