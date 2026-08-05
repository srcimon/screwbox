package dev.screwbox.core.graphics.smoke.styles;

import dev.screwbox.core.graphics.smoke.SmokeStyle;

public class HeatVisionSmokeStyle implements SmokeStyle {

    @Override
    public int apply(float red, float green, float blue, float alpha) {
        final float funR = (float) Math.sin(alpha * 2.0 * Math.PI + 0.0) * 0.5f + 0.5f;
        final float funG = (float) Math.sin(alpha * 2.0 * Math.PI + 2.094) * 0.5f + 0.5f;
        final float funB = (float) Math.sin(alpha * 2.0 * Math.PI + 4.188) * 0.5f + 0.5f;

        final int rPremult = (int) (funR * alpha * 255.0f + 0.5f);
        final int gPremult = (int) (funG * alpha * 255.0f + 0.5f);
        final int bPremult = (int) (funB * alpha * 255.0f + 0.5f);
        final int aInt = (int) (alpha * 255.0f + 0.5f);
        return packRgb(rPremult, gPremult, bPremult, aInt);
    }
}
