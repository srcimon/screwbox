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
        //TODO use Color... functions here
        final int alpha = (rgb & 0xff000000);
        final int red = (rgb & 0xff0000) >> 16;
        final int green = (rgb & 0x00ff00) >> 8;
        final int blue = (rgb & 0x0000ff);

        final int targetRed = Color.clampRgbRange(red + deltaRed);
        final int targetGreen = Color.clampRgbRange(green + deltaGreen);
        final int targetBlue = Color.clampRgbRange(blue + deltaBlue);

        return alpha | (targetRed << 16) | (targetGreen << 8) | targetBlue;
    }
}
