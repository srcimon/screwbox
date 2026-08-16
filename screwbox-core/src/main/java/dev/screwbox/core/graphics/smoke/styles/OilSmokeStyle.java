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

        // --- ETWAS DICHTERE TRANSLUZENZ-WELLE ---
        float wave = (float) ((Math.sin(alpha * Math.PI * 7.0f) + 1.0f) * 0.5f);

        // Exponent von 4.0 auf 2.5 gesenkt, um die Täler nicht ganz so extrem dünn zu pressen
        float ringVisibility = (float) Math.pow(wave, 2.5);

        // --- MINIMALE BASE-OPAZITÄT ANGEHOBEN ---
        // Von 0.05f auf 0.18f erhöht: Die transparenten Bereiche lassen den Hintergrund
        // immer noch durchscheinen, sind aber spürbar satter und weniger flüchtig.
        float baseOpacity = 0.18f + 0.82f * ringVisibility;
        float translucentAlpha = alpha * baseOpacity;

        // --- ÖLIGER FARBSHIFT ---
        float oilShift = (float) Math.cos(alpha * Math.PI * 7.0f);

        float funR = red;
        float funG = green;
        float funB = blue;

        // Basis-Ölfarben berechnen
        if (oilShift > 0.2f) {
            funG = Math.min(1.0f, green + 0.10f * oilShift);
            funB = Math.min(1.0f, blue  + 0.15f * oilShift);
        } else if (oilShift < -0.2f) {
            funR = Math.min(1.0f, red  + 0.15f * -oilShift);
            funB = Math.min(1.0f, blue + 0.08f * -oilShift);
        }

        // --- ÖL-GEFÄRBTE HIGHLIGHTS ---
        float highlightFactor = (float) Math.pow(wave, 2.0f);

        if (oilShift > 0.0f) {
            // Petrol-Highlight-Boost
            funR = Math.min(1.0f, funR + highlightFactor * 0.02f);
            funG = Math.min(1.0f, funG + highlightFactor * 0.12f * oilShift);
            funB = Math.min(1.0f, funB + highlightFactor * 0.18f * oilShift);
        } else {
            // Magenta-Highlight-Boost
            funR = Math.min(1.0f, funR + highlightFactor * 0.18f * -oilShift);
            funG = Math.min(1.0f, funG + highlightFactor * 0.02f);
            funB = Math.min(1.0f, funB + highlightFactor * 0.12f * -oilShift);
        }

        // Farbkanäle mit der angepassten Alpha-Zuweisung berechnen
        final int rPremult = (int) (funR * translucentAlpha * 255.0f + 0.5f);
        final int gPremult = (int) (funG * translucentAlpha * 255.0f + 0.5f);
        final int bPremult = (int) (funB * translucentAlpha * 255.0f + 0.5f);
        final int aInt = (int) (translucentAlpha * 255.0f + 0.5f);

        return packRgb(rPremult, gPremult, bPremult, aInt);
    }


}
