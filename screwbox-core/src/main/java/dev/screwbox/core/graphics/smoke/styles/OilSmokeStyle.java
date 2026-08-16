package dev.screwbox.core.graphics.smoke.styles;

import dev.screwbox.core.graphics.smoke.SmokeStyle;

//TODO document
//TODO test
//TODO changelog
public class OilSmokeStyle implements SmokeStyle {
    @Override
    public int apply(float red, float green, float blue, float alpha) {
        if (alpha < 0.12f) {
            return 0;
        }

        float wave = (float) ((Math.sin(alpha * Math.PI * 7.0f) + 1.0f) * 0.5f);

        float ringVisibility = (float) Math.pow(wave, 4.0);

        float baseOpacity = 0.05f + 0.95f * ringVisibility;
        float translucentAlpha = alpha * baseOpacity;

        float oilShift = (float) Math.cos(alpha * Math.PI * 7.0f);

        float funR = red;
        float funG = green;
        float funB = blue;

        if (oilShift > 0.2f) {
            funG = Math.min(1.0f, green + 0.10f * oilShift);
            funB = Math.min(1.0f, blue  + 0.15f * oilShift);
        } else if (oilShift < -0.2f) {
            funR = Math.min(1.0f, red  + 0.15f * -oilShift);
            funB = Math.min(1.0f, blue + 0.08f * -oilShift);
        }

        float highlightFactor = Math.max(0.0f, wave);
        funR = Math.min(1.0f, funR + highlightFactor * 0.06f);
        funG = Math.min(1.0f, funG + highlightFactor * 0.06f);
        funB = Math.min(1.0f, funB + highlightFactor * 0.06f);

        final int rPremult = (int) (funR * translucentAlpha * 255.0f + 0.5f);
        final int gPremult = (int) (funG * translucentAlpha * 255.0f + 0.5f);
        final int bPremult = (int) (funB * translucentAlpha * 255.0f + 0.5f);
        final int aInt = (int) (translucentAlpha * 255.0f + 0.5f);

        return packRgb(rPremult, gPremult, bPremult, aInt);
    }

}
