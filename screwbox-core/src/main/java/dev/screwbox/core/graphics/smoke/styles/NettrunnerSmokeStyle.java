package dev.screwbox.core.graphics.smoke.styles;

import dev.screwbox.core.graphics.smoke.SmokeStyle;
import dev.screwbox.core.graphics.smoke.internal.SmokeProjector;

public class NettrunnerSmokeStyle implements SmokeStyle {
    @Override
    public int apply(float red, float green, float blue, float alpha) {
        if (alpha < 0.10f) {
            return 0;
        }

        // 1. Schärfere Quantisierung für einen saubereren "Pixel-Art"-Stufenverlauf
        float steppedAlpha = (int)(alpha * 12.0f) / 12.0f;

        // Basis-Farbe: Tiefes Cyber-Violett
        float funR = Math.clamp(steppedAlpha * 0.35f, 0.0f, 1.0f);
        float funG = Math.clamp(0.05f + steppedAlpha * 0.1f, 0.0f, 1.0f);
        float funB = Math.clamp(0.65f + steppedAlpha * 0.35f, 0.0f, 1.0f);

        // 2. Chromatische Aberration (Simuliert Linsen-/Signalfehler basierend auf Alpha)
        // Erzeugt ein subtiles, wellenförmiges Farbflackern in den Mitteltönen
        float wave = (float) Math.sin(alpha * 50.0f);
        if (wave > 0.85f) {
            funR = Math.clamp(funR + 0.25f, 0.0f, 1.0f); // Rote Farbverschiebung
        } else if (wave < -0.85f) {
            funB = Math.clamp(funB - 0.20f, 0.0f, 1.0f); // Blaue Farbverschiebung
        }

        // 3. Bit-Masken-Rauschen: Schneller und organischer als der Modulo-Operator
        // Injiziert vereinzelt giftgrüne Datenfragmente
        if (((Float.floatToIntBits(alpha) & 0x1F) == 0)) {
            funG = Math.clamp(funG + 0.4f, 0.0f, 1.0f);
        }

        // 4. Interpolierter Overload-Kern: Sanfterer, aber intensiverer Crash-Übergang
        if (alpha > 0.70f) {
            // Ein weicherer Taper verhindert zu harte, hässliche Farbabrisse im Zentrum
            float t = (alpha - 0.70f) / 0.30f;
            funR = Math.clamp(funR * (1.0f - t) + 1.0f * t, 0.0f, 1.0f);
            funG = Math.clamp(funG * (1.0f - t) + 0.15f * t, 0.0f, 1.0f);
            funB = Math.clamp(funB * (1.0f - t) + 0.0f * t, 0.0f, 1.0f);
        }

        // Premultiplied ARGB Berechnung
        int rPremult = (int) (funR * alpha * 255.0f + 0.5f);
        int gPremult = (int) (funG * alpha * 255.0f + 0.5f);
        int bPremult = (int) (funB * alpha * 255.0f + 0.5f);
        int aInt = (int) (alpha * 255.0f + 0.5f);

        return packRgb(rPremult, gPremult, bPremult, aInt);
    }
}
