package de.suzufa.screwbox.core.graphics.internal;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

class InvertLightmapImageWithMinOpacityFilter extends RGBImageFilter {

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final Color currentColor = new Color(rgb, true);
        final int alpha = currentColor.getAlpha();
        return new Color(
                currentColor.getRed(),
                currentColor.getGreen(),
                currentColor.getBlue(),
                Math.max(255 - alpha, 10)).getRGB();
    }

}
