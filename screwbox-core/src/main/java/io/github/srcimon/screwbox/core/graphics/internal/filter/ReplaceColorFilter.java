package io.github.srcimon.screwbox.core.graphics.internal.filter;

import io.github.srcimon.screwbox.core.graphics.Color;

import java.awt.image.RGBImageFilter;

public class ReplaceColorFilter extends RGBImageFilter {

    private final int oldRgb;
    private final int newRgb;

    public ReplaceColorFilter(final Color oldColor, final Color newColor) {
        oldRgb = oldColor.rgb();
        newRgb = newColor.rgb();
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        return rgb == oldRgb ? newRgb : rgb;
    }
}
