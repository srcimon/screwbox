package dev.screwbox.core.graphics.smoke.styles;

import dev.screwbox.core.graphics.smoke.SmokeStyle;

public class ComicSmokeStyle implements SmokeStyle {
    @Override
    public int apply(float red, float green, float blue, float alpha) {
        float maxChannel = Math.max(red, Math.max(green, blue));
        float funR = (red == maxChannel) ? Math.clamp(red * 1.8f, 0.0f, 1.0f) : red * 0.3f;
        float funG = (green == maxChannel) ? Math.clamp(green * 1.8f, 0.0f, 1.0f) : green * 0.3f;
        float funB = (blue == maxChannel) ? Math.clamp(blue * 1.8f, 0.0f, 1.0f) : blue * 0.3f;

        if (maxChannel > 0.8f) {
            funR = Math.clamp(funR + 0.2f, 0.0f, 1.0f);
            funG = Math.clamp(funG + 0.2f, 0.0f, 1.0f);
            funB = Math.clamp(funB + 0.2f, 0.0f, 1.0f);
        }

        funR = Math.round(funR * 2.0f) / 2.0f;
        funG = Math.round(funG * 2.0f) / 2.0f;
        funB = Math.round(funB * 2.0f) / 2.0f;

        int rPremult = (int) (funR * alpha * 255.0f + 0.5f);
        int gPremult = (int) (funG * alpha * 255.0f + 0.5f);
        int bPremult = (int) (funB * alpha * 255.0f + 0.5f);
        int aInt = (int) (alpha * 255.0f + 0.5f);
        return packRgb(rPremult, gPremult, bPremult, aInt);
    }
}
