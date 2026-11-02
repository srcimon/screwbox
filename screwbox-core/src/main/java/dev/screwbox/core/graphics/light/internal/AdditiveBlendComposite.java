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

            // ========================================================
            // KORREKTUR: Sättigung (Farbigkeit) des Quell-Lichts messen
            // ========================================================
            int maxColor = Math.max(rSrc, Math.max(gSrc, bSrc));
            int minColor = Math.min(rSrc, Math.min(gSrc, bSrc));

            // Sättigung ist 0.0 bei reinem Weiß/Grau/Schwarz und 1.0 bei reinem Farbton
            float saturation = (maxColor == 0) ? 0.0f : (float)(maxColor - minColor) / maxColor;

            // Reines Additiv-Dimm-Verhalten für Weiß (damit es nicht grau wird)
            int addR = Math.min(255, rDst + (int)(rSrc * factor));
            int addG = Math.min(255, gDst + (int)(gSrc * factor));
            int addB = Math.min(255, bDst + (int)(bSrc * factor));

            // Interpoliertes Verhalten für bunte Farben (verhindert das Ausbleichen zu Weiß)
            int mixR = (int) (rDst * (1.0f - factor) + rSrc * factor);
            int mixG = (int) (gDst * (1.0f - factor) + gSrc * factor);
            int mixB = (int) (bDst * (1.0f - factor) + bSrc * factor);

            // Überblenden zwischen Additiv (Weiß) und Mix (Farbe) basierend auf der Sättigung
            int rFinal = (int) (addR * (1.0f - saturation) + mixR * saturation);
            int gFinal = (int) (addG * (1.0f - saturation) + mixG * saturation);
            int bFinal = (int) (addB * (1.0f - saturation) + mixB * saturation);

            int aFinal = Math.min(255, (int)(aSrc * extraAlpha) + aDst);

            dstPixels[i] = (aFinal << 24) | (rFinal << 16) | (gFinal << 8) | bFinal;
        }

        dstOut.setDataElements(0, 0, w, h, dstPixels);
    }
}