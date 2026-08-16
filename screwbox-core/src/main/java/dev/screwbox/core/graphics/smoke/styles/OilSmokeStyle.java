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

        // --- SCHARFE TRANSLUZENZ-WELLEN ---
        float wave = (float) ((Math.sin(alpha * Math.PI * 7.0f) + 1.0f) * 0.5f);
        float ringVisibility = (float) Math.pow(wave, 4.0);

        // Fast vollständige Transparenz in den Wellentälern
        float baseOpacity = 0.05f + 0.95f * ringVisibility;
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

        // --- ÖL-GEFÄRBTE HIGHLIGHTS AN DEN INTENSIVSTEN STELLEN ---
        // Die Welle (0.0 bis 1.0) bestimmt die Intensität des Highlights.
        float highlightFactor = (float) Math.pow(wave, 2.0f); // Macht die Highlights präziser auf den Spitzen

        // Je nach Phase des oilShift (Petrol oder Magenta) färben wir das Highlight
        // intensiv mit der entsprechenden Öl-Nuance ein, anstatt es weiß zu bleichen.
        if (oilShift > 0.0f) {
            // Petrol-Highlight-Boost (Grün/Blau-Kanal dominieren das Glühen)
            funR = Math.min(1.0f, funR + highlightFactor * 0.02f);
            funG = Math.min(1.0f, funG + highlightFactor * 0.12f * oilShift);
            funB = Math.min(1.0f, funB + highlightFactor * 0.18f * oilShift);
        } else {
            // Magenta-Highlight-Boost (Rot/Blau-Kanal dominieren das Glühen)
            funR = Math.min(1.0f, funR + highlightFactor * 0.18f * -oilShift);
            funG = Math.min(1.0f, funG + highlightFactor * 0.02f);
            funB = Math.min(1.0f, funB + highlightFactor * 0.12f * -oilShift);
        }

        // Farbkanäle mit der transluzenten Alpha-Zuweisung berechnen
        final int rPremult = (int) (funR * translucentAlpha * 255.0f + 0.5f);
        final int gPremult = (int) (funG * translucentAlpha * 255.0f + 0.5f);
        final int bPremult = (int) (funB * translucentAlpha * 255.0f + 0.5f);
        final int aInt = (int) (translucentAlpha * 255.0f + 0.5f);

        return packRgb(rPremult, gPremult, bPremult, aInt);
    }


}
