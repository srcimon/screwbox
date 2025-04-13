package dev.screwbox.core.graphics.internal.filter;

import java.awt.*;
import java.awt.image.RGBImageFilter;

public class InvertImageOpacityFilter extends RGBImageFilter {

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final Color current = new Color(rgb, true);
        final int alpha = 255 - current.getAlpha();
        return new Color(current.getRed(), current.getGreen(), current.getBlue(), alpha).getRGB();
    }

}
