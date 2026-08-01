package dev.screwbox.core.graphics.smoke.styles;

import dev.screwbox.core.graphics.smoke.SmokeStyle;

public class FireSmokeStyle implements SmokeStyle {
    @Override
    public int apply(float red, float green, float blue, float alpha) {
        float intensity = (red + green + blue) / 3.0f * (alpha);
        float funR = Math.clamp(intensity * 2.5f, 0.0f, 1.0f);
        float funG = Math.clamp((intensity - 0.3f) * 2.0f, 0.0f, 1.0f);
        float funB = Math.clamp((intensity - 0.7f) * 4.0f, 0.0f, 1.0f);
        float funA = Math.max(intensity * 1.5f, 0.0f);

        int rPremult = (int) (funR * funA * 255.0f + 0.5f);
        int gPremult = (int) (funG * funA * 255.0f + 0.5f);
        int bPremult = (int) (funB * funA * 255.0f + 0.5f);
        int aInt = (int) (funA * 255.0f + 0.5f);
        return packRgb(rPremult, gPremult << 8, bPremult, aInt);
    }
}
