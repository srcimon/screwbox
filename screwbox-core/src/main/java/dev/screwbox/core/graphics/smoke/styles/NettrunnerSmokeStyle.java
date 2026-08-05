package dev.screwbox.core.graphics.smoke.styles;

import dev.screwbox.core.graphics.smoke.SmokeStyle;
import dev.screwbox.core.graphics.smoke.internal.SmokeProjector;

public class NettrunnerSmokeStyle implements SmokeStyle {
    @Override
    public int apply(float red, float green, float blue, float alpha) {
        // Sanftes Ausblenden am Rand gegen hartes Aufpoppen
        if (alpha < 0.10f) {
            return 0;
        }

        // 1. REPARIERT: Kontinuierliche Ring-Welle (Niedrigere Frequenz für Stabilität)
        // Das Ergebnis wandert weich zwischen 0.0 und 1.0 hin und her
        float ringWave = (float) Math.sin(alpha * 24.0f);
        float ringIntensity = (ringWave + 1.0f) * 0.5f;

        // Basis-Farbe: Sattes Cyber-Violett
        float funR = Math.clamp(alpha * 0.35f, 0.0f, 1.0f);
        float funG = Math.clamp(0.05f + alpha * 0.1f, 0.0f, 1.0f);
        float funB = Math.clamp(0.65f + alpha * 0.35f, 0.0f, 1.0f);

        // 2. REPARIERT: Weiche Ring-Injektion (Keine harten IF-Sprünge mehr)
        // Die Ringe manifestieren sich als stabiler, fließender Cyan-Neon-Schimmer
        float neonG = ringIntensity * 0.25f * alpha;
        float neonB = ringIntensity * 0.20f * alpha;
        funG = Math.clamp(funG + neonG, 0.0f, 1.0f);
        funB = Math.clamp(funB + neonB, 0.0f, 1.0f);

        // Chromatische Aberration am äußersten Rand (stabil)
        float edgeShift = Math.clamp((alpha - 0.10f) / 0.40f, 0.0f, 1.0f);
        funR = Math.clamp(funR + edgeShift * 0.15f, 0.0f, 1.0f);

        // 3. Overload-Kern: Absolut flüssiger Übergang in das glühende Orange-Rot
        if (alpha > 0.70f) {
            float t = (alpha - 0.70f) / 0.30f;
            funR = Math.clamp(funR * (1.0f - t) + 1.0f * t, 0.0f, 1.0f);
            funG = Math.clamp(funG * (1.0f - t) + 0.15f * t, 0.0f, 1.0f);
            funB = Math.clamp(funB * (1.0f - t) + 0.0f * t, 0.0f, 1.0f);
        }

        // Premultiplied ARGB Berechnung (Absolut flackerfrei)
        int rPremult = (int) (funR * alpha * 255.0f + 0.5f);
        int gPremult = (int) (funG * alpha * 255.0f + 0.5f);
        int bPremult = (int) (funB * alpha * 255.0f + 0.5f);
        int aInt = (int) (alpha * 255.0f + 0.5f);

        return packRgb(rPremult, gPremult, bPremult, aInt);
    }
}
