package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.utils.FractalNoise;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;

public class UnderwaterShader extends Shader {

    private final double zoom;
    private final int distortion;

    public UnderwaterShader(final double zoom, final int distortion) {
        super("UnderwaterShader-" + zoom + "-" + distortion);
        this.zoom = zoom;//TODO validate
        this.distortion = distortion;
    }

    @Override
    public Image apply(Image source, Percent progress) {
        final BufferedImage sourceImage = ImageOperations.toBufferedImage(source);
        return ImageOperations.applyFilter(source, new RGBImageFilter() {
            @Override
            public int filterRGB(final int x, final int y, final int rgb) {
                final var zLoopX = 1 + Math.sin((progress.value()) * 2 * Math.PI) * 100;
                final var noise = FractalNoise.generateFractalNoise3d(zoom, 129312, x, y, zLoopX);
                final double sourceX = x + noise.rangeValue(-distortion, distortion);

                final var zLoopY = 1 + Math.sin(1 + +(progress.value()) * 2 * Math.PI) * 100;
                final var noiseY = FractalNoise.generateFractalNoise3d(zoom, 4201, x, y, zLoopY);
                final double sourceY = y + noiseY.rangeValue(-distortion, distortion);

                return sourceImage.getRGB(
                        (int) (Math.clamp(sourceX, 0, sourceImage.getWidth() - 1.0))
                        , (int) (Math.clamp(sourceY, 0, sourceImage.getHeight() - 1.0)));
            }
        });
    }
}
