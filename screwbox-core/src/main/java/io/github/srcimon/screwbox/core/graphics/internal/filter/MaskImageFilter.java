package io.github.srcimon.screwbox.core.graphics.internal.filter;

import io.github.srcimon.screwbox.core.graphics.Color;

import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;

public class MaskImageFilter extends RGBImageFilter {

    private final BufferedImage mask;
    private final int threshold;

    public MaskImageFilter(final BufferedImage mask, final int threshold) {
        this.mask = mask;
        this.threshold = threshold;
        //TODO validate inputs
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        int sourceX = x % mask.getWidth();
        int sourceY = x % mask.getHeight();
        int sourceRgb = mask.getRGB(sourceX, sourceY);
        int average = Color.rgb(sourceRgb).average();
        //TODO opacity?
        return average > threshold ? rgb : 0;
    }
}
