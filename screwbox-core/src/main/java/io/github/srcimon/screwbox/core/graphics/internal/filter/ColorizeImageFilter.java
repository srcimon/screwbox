package io.github.srcimon.screwbox.core.graphics.internal.filter;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.awt.image.RGBImageFilter;

public class ColorizeImageFilter extends RGBImageFilter {

    private final int deltaRed;
    private final int deltaGreen;
    private final int deltaBlue;

    public ColorizeImageFilter(int deltaRed, int deltaGreen, int deltaBlue) {
        Validate.range(deltaRed, 0, 255, "deltaRed must be in range 0 to 255");
        Validate.range(deltaGreen, 0, 255, "deltaGreen must be in range 0 to 255");
        Validate.range(deltaBlue, 0, 255, "deltaBlue must be in range 0 to 255");
        this.deltaRed = deltaRed;
        this.deltaGreen = deltaGreen;
        this.deltaBlue = deltaBlue;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        final var color = Color.rgb(rgb);
        final int targetRed = Color.clampRgbRange(color.r() + deltaRed);
        final int targetGreen = Color.clampRgbRange(color.g() + deltaGreen);
        final int targetBlue = Color.clampRgbRange(color.b() + deltaBlue);

        return color.alpha() | (targetRed << 16) | (targetGreen << 8) | targetBlue;
    }
}
