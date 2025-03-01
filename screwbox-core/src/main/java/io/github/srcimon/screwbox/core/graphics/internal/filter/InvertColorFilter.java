package io.github.srcimon.screwbox.core.graphics.internal.filter;

import java.awt.image.RGBImageFilter;

public class InvertColorFilter extends RGBImageFilter {

    @Override
    public int filterRGB(final int x,final int y, final int rgb) {
        final int a = (rgb >> 24) & 0xff;
        final int r = 255 - (rgb >> 16) & 0xff;
        final int g = 255 - (rgb >> 8) & 0xff;
        final int b = 255 - rgb & 0xff;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
