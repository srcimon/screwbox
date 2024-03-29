package io.github.srcimon.screwbox.core.graphics.internal.filter;

import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.internal.AwtMapper;

import java.awt.image.RGBImageFilter;

public class ReplaceColorFilter extends RGBImageFilter {

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
