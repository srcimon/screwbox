package dev.screwbox.core.graphics.smoke.styles;

import dev.screwbox.core.graphics.smoke.SmokeStyle;

public class FrostSmokeStyle implements SmokeStyle {

    @Override
    public int apply(float red, float green, float blue, float alpha) {
        if (alpha < 0.15f) {
            return 0;
        }

        float funR = Math.clamp(alpha * 0.4f, 0.0f, 1.0f);
        float funG = Math.clamp(0.4f + alpha * 0.5f, 0.0f, 1.0f);
        float funB = Math.clamp(0.7f + alpha * 0.3f, 0.0f, 1.0f);

        if (alpha > 0.7f) {
            funR = Math.clamp(funR + (alpha - 0.7f) * 2.0f, 0.0f, 1.0f);
            funG = Math.clamp(funG + (alpha - 0.7f) * 2.0f, 0.0f, 1.0f);
        }

        int rPremult = (int) (funR * alpha * 255.0f + 0.5f);
        int gPremult = (int) (funG * alpha * 255.0f + 0.5f);
        int bPremult = (int) (funB * alpha * 255.0f + 0.5f);
        int aInt = (int) (alpha * 255.0f + 0.5f);
        return packRgb(rPremult, gPremult, bPremult, aInt);
    }
}
