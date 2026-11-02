package dev.screwbox.core.graphics.light.internal;

import dev.screwbox.core.Percent;

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class AdditiveBlendComposite implements Composite, CompositeContext {

    private final float extraAlpha;

    // Privater Konstruktor: Instanziierung nur über getInstance()
    public AdditiveBlendComposite(final Percent alpha) {
        this.extraAlpha = (float)alpha.value();
    }


    @Override
    public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
        return this;
    }

    @Override
    public void dispose() {}

    @Override
    public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
        int w = Math.min(src.getWidth(), dstIn.getWidth());
        int h = Math.min(src.getHeight(), dstIn.getHeight());

        int[] srcPixels = new int[w * h];
        int[] dstPixels = new int[w * h];

        src.getDataElements(0, 0, w, h, srcPixels);
        dstIn.getDataElements(0, 0, w, h, dstPixels);

        for (int i = 0; i < srcPixels.length; i++) {
            int srcPixel = srcPixels[i];
            int dstPixel = dstPixels[i];

            int aSrc = (srcPixel >> 24) & 0xFF;
            int rSrc = (srcPixel >> 16) & 0xFF;
            int gSrc = (srcPixel >> 8) & 0xFF;
            int bSrc = srcPixel & 0xFF;

            int aDst = (dstPixel >> 24) & 0xFF;
            int rDst = (dstPixel >> 16) & 0xFF;
            int gDst = (dstPixel >> 8) & 0xFF;
            int bDst = dstPixel & 0xFF;

            float factor = (aSrc / 255.0f) * extraAlpha;

            // Gedimmte Werte der neuen Lichtquelle berechnen
            int dimmedRSrc = (int) (rSrc * factor);
            int dimmedGSrc = (int) (gSrc * factor);
            int dimmedBSrc = (int) (bSrc * factor);

            // Sättigung (Farbigkeit) des Quell-Lichts messen
            int maxColor = Math.max(rSrc, Math.max(gSrc, bSrc));
            int minColor = Math.min(rSrc, Math.min(gSrc, bSrc));
            float saturation = (maxColor == 0) ? 0.0f : (float)(maxColor - minColor) / maxColor;

            // 1. Reiner additiver Pfad für weißes Licht
            int addR = Math.min(255, rDst + dimmedRSrc);
            int addG = Math.min(255, gDst + dimmedGSrc);
            int addB = Math.min(255, bDst + dimmedBSrc);

            // ========================================================
            // KORREKTUR: Lichtfarben-Mischung mit Helligkeits-Begrenzung
            // ========================================================
            // Wir addieren die Farbkanäle, um die Farbmischung zu erzwingen (z. B. Rot + Blau = Lila)
            int blendedR = rDst + dimmedRSrc;
            int blendedG = gDst + dimmedGSrc;
            int blendedB = bDst + dimmedBSrc;

            // Um das Ausbrennen zu Weiß zu verhindern, ermitteln wir die jeweils höchsten
            // Einzelkanal-Intensitäten beider Lichter und nutzen das als Obergrenze (Helligkeitsdeckel)
            int maxAllowedR = Math.max(rDst, rSrc);
            int maxAllowedG = Math.max(gDst, gSrc);
            int maxAllowedB = Math.max(bDst, bSrc);
            int targetMaxBrightness = Math.max(maxAllowedR, Math.max(maxAllowedG, maxAllowedB));

            // Das gemischte Licht wird proportional herunterskaliert, falls es den Deckel sprengt
            int currentMixMax = Math.max(blendedR, Math.max(blendedG, blendedB));
            if (currentMixMax > targetMaxBrightness && currentMixMax > 0) {
                blendedR = (blendedR * targetMaxBrightness) / currentMixMax;
                blendedG = (blendedG * targetMaxBrightness) / currentMixMax;
                blendedB = (blendedB * targetMaxBrightness) / currentMixMax;
            }

            // Sicherheits-Clamp
            int mixR = Math.max(0, Math.min(255, blendedR));
            int mixG = Math.max(0, Math.min(255, blendedG));
            int mixB = Math.max(0, Math.min(255, blendedB));

            // 3. Überblenden zwischen Weiß (Additiv) und Farb-Lichtmischung
            int rFinal = (int) (addR * (1.0f - saturation) + mixR * saturation);
            int gFinal = (int) (addG * (1.0f - saturation) + mixG * saturation);
            int bFinal = (int) (addB * (1.0f - saturation) + mixB * saturation);

            int aFinal = Math.min(255, (int)(aSrc * extraAlpha) + aDst);

            dstPixels[i] = (aFinal << 24) | (rFinal << 16) | (gFinal << 8) | bFinal;
        }

        dstOut.setDataElements(0, 0, w, h, dstPixels);
    }
}