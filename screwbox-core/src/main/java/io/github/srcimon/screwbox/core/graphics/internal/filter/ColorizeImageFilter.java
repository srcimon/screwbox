package io.github.srcimon.screwbox.core.graphics.internal.filter;

import java.awt.image.RGBImageFilter;

public class ColorizeImageFilter extends RGBImageFilter {

    private final int deltaRed;
    private final int deltaGreen;
    private final int deltaBlue;

    public ColorizeImageFilter(int deltaRed, int deltaGreen, int deltaBlue) {
        //TODO validate
        this.deltaRed = deltaRed;
        this.deltaGreen = deltaGreen;
        this.deltaBlue = deltaBlue;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        final int alpha = (rgb & 0xff000000);
        final int red = (rgb & 0xff0000) >> 16;
        final int green = (rgb & 0x00ff00) >> 8;
        final int blue = (rgb & 0x0000ff);

        final int targetRed = Math.max(0, Math.min(0xff, red + deltaRed));
        final int targetGreen = Math.max(0, Math.min(0xff, green + deltaGreen));
        final int targetBlue = Math.max(0, Math.min(0xff, blue + deltaBlue));

        return alpha | (targetRed << 16) | (targetGreen << 8) | targetBlue;
    }
}
