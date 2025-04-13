package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;
import java.awt.image.RGBImageFilter;
import java.io.Serial;

/**
 * Converts image into greyscale image.
 *
 * @since 2.15.0
 */
public class GreyscaleShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final RGBImageFilter GREYSCALE_FILTER = new RGBImageFilter() {
        @Override
        public int filterRGB(final int x, final int y, final int rgb) {
            return Color.rgb(rgb).greyscale().rgb();
        }
    };

    public GreyscaleShader() {
        super("greyscale", false);
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        return ImageOperations.applyFilter(source, GREYSCALE_FILTER);
    }
}
