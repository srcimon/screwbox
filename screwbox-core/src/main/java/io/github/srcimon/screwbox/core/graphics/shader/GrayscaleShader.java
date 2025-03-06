package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;
import java.awt.image.RGBImageFilter;

/**
 * Converts image into grayscale image.
 *
 * @since 2.15.0
 */
public class GrayscaleShader extends Shader {

    private static final RGBImageFilter GRAYSCALE_FILTER = new RGBImageFilter() {
        @Override
        public int filterRGB(final int x, final int y, final int rgb) {
            final int a = (rgb >> 24) & 0xff;
            final int r = (rgb >> 16) & 0xff;
            final int g = (rgb >> 8) & 0xff;
            final int b = rgb & 0xff;
            final int average = (r + g + b) / 3;
            return (a << 24) | (average << 16) | (average << 8) | average;
        }
    };

    public GrayscaleShader() {
        super("grayscale", false);
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        return ImageOperations.applyFilter(source, GRAYSCALE_FILTER);
    }
}
