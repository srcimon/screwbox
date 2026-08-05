package dev.screwbox.core.graphics.smoke.styles;

import dev.screwbox.core.graphics.smoke.SmokeOptions;
import dev.screwbox.core.graphics.smoke.SmokeStyle;

/**
 * A {@link SmokeStyle} using the original colors. Used by default.
 *
 * @see SmokeOptions
 * @since 3.33.0
 */
public class TrueColorSmokeStyle implements SmokeStyle {

    @Override
    public int apply(final float red, final float green, final float blue, final float alpha) {
        final int rPremult = (int) (red * alpha * 255.0f + 0.5f);
        final int gPremult = (int) (green * alpha * 255.0f + 0.5f);
        final int bPremult = (int) (blue * alpha * 255.0f + 0.5f);
        final int aInt = (int) (alpha * 255.0f + 0.5f);
        return packRgb(rPremult, gPremult, bPremult, aInt);
    }
}
