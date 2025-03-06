package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;
import java.awt.image.RGBImageFilter;

/**
 * Inverts all colors of the image.
 *
 * @since 2.15.0
 */
public class InvertColorShader extends Shader {

    private static final RGBImageFilter INVERT_COLOR_FILTER = new RGBImageFilter() {

        @Override
        public int filterRGB(int x, int y, int rgb) {
            return Color.rgb(rgb).invert().rgb();
        }
    };

    public InvertColorShader() {
        super("invert-colors", false);
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        return ImageOperations.applyFilter(source, INVERT_COLOR_FILTER);
    }
}
