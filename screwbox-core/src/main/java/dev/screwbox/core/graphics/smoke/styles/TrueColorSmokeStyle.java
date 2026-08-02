package dev.screwbox.core.graphics.smoke.styles;

import dev.screwbox.core.graphics.smoke.SmokeStyle;

public class TrueColorSmokeStyle implements SmokeStyle {

    @Override
    public int apply(float red, float green, float blue, float alpha) {
        int rPremult = (int) (red * alpha * 255.0f + 0.5f);
        int gPremult = (int) (green * alpha * 255.0f + 0.5f);
        int bPremult = (int) (blue * alpha * 255.0f + 0.5f);
        int aInt = (int) (alpha * 255.0f + 0.5f);
        return packRgb(rPremult, gPremult, bPremult, aInt);
    }
}
