package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
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
            return Color.rgb(rgb).grayscale().rgb();

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
