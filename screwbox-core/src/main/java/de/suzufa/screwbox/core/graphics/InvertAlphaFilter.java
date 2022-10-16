package de.suzufa.screwbox.core.graphics;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

//TODO: test
class InvertAlphaFilter extends RGBImageFilter {

    @Override
    public int filterRGB(int x, int y, int rgb) {
        Color currentColor = new Color(rgb, true);
        int alpha = currentColor.getAlpha();
        return new Color(
                currentColor.getRed(),
                currentColor.getGreen(),
                currentColor.getBlue(),
                Math.max(255 - alpha, 40)).getRGB();
    }

}
