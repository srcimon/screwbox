package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;
import java.awt.image.RGBImageFilter;

import static java.util.Objects.requireNonNull;

/**
 * Draw image silhouette in specified color.
 *
 * @since 2.18.0
 */
public class SilhouetteShader extends Shader {

    private final Color color;

    public SilhouetteShader(final Color color) {
        super("SilhouetteShader-" + color.hex(), false);
        this.color = requireNonNull(color, "color must not be null");
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final int targetRgb = color.rgb();
        return ImageOperations.applyFilter(source, new RGBImageFilter() {

            @Override
            public int filterRGB(final int x, final int y, final int rgb) {
                return rgb == 0 ? 0 : targetRgb;
            }
        });
    }
}
