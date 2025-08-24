package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.utils.FractalNoise;
import dev.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;

/**
 * Creates an underwater distortion effect on the image.
 *
 * @since 3.7.0
 */
public class UnderwaterShader extends Shader {

    private final double zoom;
    private final int distortion;

    public UnderwaterShader(final double zoom, final int distortion) {
        super("UnderwaterShader-" + zoom + "-" + distortion);
        Validate.range(zoom, 1, 100, "zoom must be in range 1 to 100");
        Validate.range(distortion, 1, 32, "distortion must be in range 1 to 32");
        this.zoom = zoom;
        this.distortion = distortion;
    }

    @Override
    public Image apply(Image source, Percent progress) {
        final BufferedImage sourceImage = ImageOperations.cloneImage(source);
        return ImageOperations.applyFilter(source, new RGBImageFilter() {
            @Override
            public int filterRGB(final int x, final int y, final int rgb) {
                final var zLoopX = 1 + Math.sin(progress.value() * 2.0 * Math.PI) * 100;
                final var noiseX = FractalNoise.generateFractalNoise3d(zoom, 129312, x, y, zLoopX);
                final double sourceX = x * 1.0 + noiseX.rangeValue(-distortion, distortion);

                final var zLoopY = 1 + Math.sin(1 + progress.value() * 2.0 * Math.PI) * 100;
                final var noiseY = FractalNoise.generateFractalNoise3d(zoom, 4201, x, y, zLoopY);
                final double sourceY = y * 1.0 + noiseY.rangeValue(-distortion, distortion);

                return sourceImage.getRGB(
                        (int) (Math.clamp(sourceX, 0, sourceImage.getWidth() - 1.0))
                        , (int) (Math.clamp(sourceY, 0, sourceImage.getHeight() - 1.0)));
            }
        });
    }
}
