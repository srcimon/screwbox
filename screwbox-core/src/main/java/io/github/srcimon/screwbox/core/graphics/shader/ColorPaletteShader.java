package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.image.RGBImageFilter;
import java.io.Serial;
import java.util.Set;
import java.util.stream.Stream;

import static io.github.srcimon.screwbox.core.Percent.max;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

/**
 * Reduces the picture to the specified colors.
 *
 * @since 2.18.0
 */
public class ColorPaletteShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Set<Color> colorPalette;

    /**
     * Creates a new instance using the specified colors.
     */
    public ColorPaletteShader(final Set<Color> colorPalette) {
        super("ColorPaletteShader-" + ignoreOpacity(colorPalette).map(Color::hex).sorted().collect(joining("-")), false);
        this.colorPalette = ignoreOpacity(colorPalette).collect(toSet());
        Validate.notEmpty(this.colorPalette, "at least one color in palette is needed");
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        return ImageOperations.applyFilter(source, new RGBImageFilter() {

            @Override
            public int filterRGB(int x, int y, int rgb) {
                // fast process transparent pixels
                if (rgb == 0) {
                    return 0;
                }

                final var inColor = Color.rgb(rgb);
                return getMostMatchingColor(inColor).opacity(inColor.opacity()).rgb();
            }
        });
    }

    private Color getMostMatchingColor(final Color color) {
        Color nearest = Color.TRANSPARENT;
        double nearestDistance = Double.MAX_VALUE;
        for (var paletteColor : colorPalette) {
            double difference = paletteColor.difference(color);
            if (nearest == Color.TRANSPARENT || nearestDistance > difference) {
                nearest = paletteColor;
                nearestDistance = difference;
            }
        }
        return nearest;
    }

    private static Stream<Color> ignoreOpacity(final Set<Color> colorPalette) {
        return colorPalette.stream().map(color -> color.opacity(max())).distinct();
    }
}
