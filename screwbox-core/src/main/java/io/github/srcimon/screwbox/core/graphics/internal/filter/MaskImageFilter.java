package io.github.srcimon.screwbox.core.graphics.internal.filter;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;

import java.awt.image.RGBImageFilter;

public class MaskImageFilter extends RGBImageFilter {

    private final Frame mask;
    private final int threshold;

    public MaskImageFilter(final Frame mask, final int threshold) {
        this.mask = mask;
        this.threshold = threshold;
        //TODO validate inputs
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        if (rgb == 0) {
            return 0;
        }
        int sourceX = x % mask.width();
        int sourceY = y % mask.height();
        Color color = mask.colorAt(sourceX, sourceY);
        int average = color.average();
        int distance = Math.max(0, average - threshold);

        return Color.rgb(rgb).opacity(Percent.of(distance / 255.0)).rgb();//TODO speed up this
    }
}
