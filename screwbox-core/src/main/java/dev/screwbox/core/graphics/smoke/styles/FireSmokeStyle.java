package dev.screwbox.core.graphics.smoke.styles;

import dev.screwbox.core.graphics.smoke.SmokeOptions;
import dev.screwbox.core.graphics.smoke.SmokeStyle;

/**
 * A fire like {@link SmokeStyle}.
 *
 * @see SmokeOptions
 * @since 3.33.0
 */
public class FireSmokeStyle implements SmokeStyle {

    @Override
    public int apply(float red, float green, float blue, float alpha) {
        if (alpha < 0.12f) {
            return 0;
        }

        final float funR;
        final float funG;
        final float funB;

        if (alpha < 0.40f) {
            float t = (alpha - 0.12f) / 0.28f;
            funR = 0.25f * t;
            funG = 0.02f * t;
            funB = 0.02f * t;
        } else if (alpha < 0.75f) {
            float t = (alpha - 0.40f) / 0.35f;
            funR = 0.25f * (1.0f - t) + 0.90f * t;
            funG = 0.02f * (1.0f - t) + 0.35f * t;
            funB = 0.02f * (1.0f - t) + 0.05f * t;
        } else {
            float t = (alpha - 0.75f) / 0.25f;
            funR = 0.90f * (1.0f - t) + 1.0f * t;
            funG = 0.35f * (1.0f - t) + 0.85f * t;
            funB = 0.05f * (1.0f - t) + 0.30f * t;
        }

        final int rPremult = (int) (funR * alpha * 255.0f + 0.5f);
        final int gPremult = (int) (funG * alpha * 255.0f + 0.5f);
        final int bPremult = (int) (funB * alpha * 255.0f + 0.5f);
        final int aInt = (int) (alpha * 255.0f + 0.5f);

        return packRgb(rPremult, gPremult, bPremult, aInt);
    }
}
