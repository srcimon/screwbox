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
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        if (rgb == 0) {
            return 0;
        }
        final Color maskColor = mask.colorAt(x % mask.width(), y % mask.height());
        final int distance = Math.max(0, maskColor.brightness() - threshold);
        final Color color = Color.rgb(rgb);
        final Percent calculatedOpacity = Percent.of(distance / 255.0 * color.opacity().value() );
        return color.opacity(calculatedOpacity).rgb();
    }
}
