package dev.screwbox.core.graphics.internal.filter;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;

import java.awt.image.RGBImageFilter;

public class MaskImageFilter extends RGBImageFilter {

    private final Frame mask;
    private final int threshold;
    private final boolean useBinaryBlend;

    public MaskImageFilter(final Frame mask, final int threshold, final boolean useBinaryBlend) {
        this.mask = mask;
        this.threshold = threshold;
        this.useBinaryBlend = useBinaryBlend;
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        if (rgb == 0) {
            return 0;
        }
        final Color maskColor = mask.colorAt(x % mask.width(), y % mask.height());

        final int distance = Math.clamp(maskColor.brightness() - threshold * 2L + 255, 0, 255);
        if (useBinaryBlend && distance < 255) {
            return 0;
        }
        final Color color = Color.rgb(rgb);
        final Percent calculatedOpacity = Percent.of(distance / 255.0 * color.opacity().value());
        return color.opacity(calculatedOpacity).rgb();
    }
}
