package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;
import java.awt.image.RGBImageFilter;
import java.io.Serial;

/**
 * Inverts all colors of the image.
 *
 * @since 2.15.0
 */
public class InvertColorShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

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
