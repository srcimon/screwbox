package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;
import java.awt.image.RGBImageFilter;
import java.io.Serial;
import java.util.Set;

import static java.util.stream.Collectors.joining;

//TODO document
//TODO implement
public class ColorPaletteShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;
    private final Set<Color> colorPalette;

    public ColorPaletteShader(final Set<Color> colorPalette) {
        super("ColorPaletteShader-" + colorPalette.stream().map(Color::hex).collect(joining("-")), false);
        //TODO validate not empty!
        this.colorPalette = colorPalette;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        return ImageOperations.applyFilter(source, new RGBImageFilter() {

            @Override
            public int filterRGB(int x, int y, int rgb) {
                //TODO really helps?
                if(rgb==0) {
                    return 0;
                }

                var in = Color.rgb(rgb);
                return getNearestColor(in).opacity(in.opacity()).rgb();
            }
        });
    }

    private Color getNearestColor(final Color color) {
        Color nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        for(var paletteColor : colorPalette) {
            double difference = paletteColor.difference(color);
            if(nearest==null || nearestDistance > difference) {
                nearest = paletteColor;
                nearestDistance = difference;
            }
        }
        return nearest;
    }
}
