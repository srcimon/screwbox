package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.utils.FractalNoise;
import dev.screwbox.core.utils.PerlinNoise;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;

public class UnderwaterShader extends Shader {

    public UnderwaterShader() {
        super("UnderwaterShader");
    }

    @Override
    public Image apply(Image source, Percent progress) {
        final BufferedImage s = ImageOperations.toBufferedImage(source);
        return ImageOperations.applyFilter(source, new RGBImageFilter() {
            @Override
            public int filterRGB(final int x, final int y, final int rgb) {
                var loopOffset = 1 + Math.sin((progress.value()) * Math.PI) * 60;

                var noise = FractalNoise.generateFractalNoise3d(20, 129312, x, y, loopOffset);
                var noiseY = FractalNoise.generateFractalNoise3d(20, 4201, x, y, loopOffset);
                final double sourceX = x + noise.rangeValue(-8,8);
                final double sourceY = y +  noiseY.rangeValue(-8,8);
                return s.getRGB(
                        (int) (Math.clamp(sourceX, 0, s.getWidth() - 1.0))
                        , (int) (Math.clamp(sourceY, 0, s.getHeight() - 1.0)));
            }
        });
    }
}
