package dev.screwbox.core.graphics.internal.filter;


import dev.screwbox.core.graphics.Color;

import java.awt.image.RGBImageFilter;

public class MaskColorFilter extends RGBImageFilter {

    private final int maskRgb;

    public MaskColorFilter(final Color color) {
        this.maskRgb = color.rgb();
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        return rgb == maskRgb ? rgb : 0;
    }
}
