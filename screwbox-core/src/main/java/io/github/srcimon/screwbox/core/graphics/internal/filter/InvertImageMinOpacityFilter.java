package io.github.srcimon.screwbox.core.graphics.internal.filter;

import java.awt.*;
import java.awt.image.RGBImageFilter;

public class InvertImageMinOpacityFilter extends RGBImageFilter {

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final Color current = new Color(rgb, true);
        final int alpha = Math.max(255 - current.getAlpha(), 10);
        return new Color(current.getRed(), current.getGreen(), current.getBlue(), alpha).getRGB();
    }

}
