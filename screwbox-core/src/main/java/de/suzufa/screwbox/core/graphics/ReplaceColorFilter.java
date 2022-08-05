package de.suzufa.screwbox.core.graphics;

import java.awt.image.RGBImageFilter;

import de.suzufa.screwbox.core.graphics.internal.AwtMapper;

class ReplaceColorFilter extends RGBImageFilter {

    private final int oldColorAwt;
    private final int newColorAwt;

    public ReplaceColorFilter(final Color oldColor, final Color newColor) {
        oldColorAwt = AwtMapper.toAwtColor(oldColor).getRGB();
        newColorAwt = AwtMapper.toAwtColor(newColor).getRGB();
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        return rgb == oldColorAwt ? newColorAwt : rgb;
    }

}
