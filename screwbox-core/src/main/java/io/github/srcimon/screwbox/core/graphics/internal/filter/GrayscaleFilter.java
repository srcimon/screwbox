package io.github.srcimon.screwbox.core.graphics.internal.filter;

import java.awt.image.RGBImageFilter;

public class GrayscaleFilter extends RGBImageFilter {

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final int a = (rgb >> 24) & 0xff;
        final int r = (rgb >> 16) & 0xff;
        final int g = (rgb >> 8) & 0xff;
        final int b = rgb & 0xff;
        final int average = (r + g + b) / 3;
        return (a << 24) | (average << 16) | (average << 8) | average;
    }
}
