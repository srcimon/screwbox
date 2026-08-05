package dev.screwbox.core.graphics.smoke.styles;

import dev.screwbox.core.graphics.smoke.SmokeStyle;

public class NettrunnerSmokeStyle implements SmokeStyle {
    @Override
    public int apply(float red, float green, float blue, float alpha) {
        if (alpha < 0.10f) { // hard edges
            return 0;
        }

        final float ringWave = (float) Math.sin(alpha * 24.0f);
        final float ringIntensity = (ringWave + 1.0f) * 0.5f;

        float funR = Math.clamp(alpha * 0.35f, 0.0f, 1.0f);
        float funG = Math.clamp(0.05f + alpha * 0.1f, 0.0f, 1.0f);
        float funB = Math.clamp(0.65f + alpha * 0.35f, 0.0f, 1.0f);

        float neonG = ringIntensity * 0.25f * alpha;
        float neonB = ringIntensity * 0.20f * alpha;
        funG = Math.clamp(funG + neonG, 0.0f, 1.0f);
        funB = Math.clamp(funB + neonB, 0.0f, 1.0f);

        final float edgeShift = Math.clamp((alpha - 0.10f) / 0.40f, 0.0f, 1.0f);
        funR = Math.clamp(funR + edgeShift * 0.15f, 0.0f, 1.0f);

        if (alpha > 0.70f) {
            final float t = (alpha - 0.70f) / 0.30f;
            funR = Math.clamp(funR * (1.0f - t) + t, 0.0f, 1.0f);
            funG = Math.clamp(funG * (1.0f - t) + 0.15f * t, 0.0f, 1.0f);
            funB = Math.clamp(funB * (1.0f - t) + 0.0f * t, 0.0f, 1.0f);
        }

        final int rPremult = (int) (funR * alpha * 255.0f + 0.5f);
        final int gPremult = (int) (funG * alpha * 255.0f + 0.5f);
        final int bPremult = (int) (funB * alpha * 255.0f + 0.5f);
        final int aInt = (int) (alpha * 255.0f + 0.5f);

        return packRgb(rPremult, gPremult, bPremult, aInt);
    }
}
