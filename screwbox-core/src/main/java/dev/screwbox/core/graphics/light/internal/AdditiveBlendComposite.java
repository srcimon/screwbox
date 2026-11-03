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
        int width = Math.min(src.getWidth(), dstIn.getWidth());
        int height = Math.min(src.getHeight(), dstIn.getHeight());

        final int[] sourcePixels = new int[width * height];
        final int[] destinationPixel = new int[width * height];

        src.getDataElements(0, 0, width, height, sourcePixels);
        dstIn.getDataElements(0, 0, width, height, destinationPixel);

        for (int i = 0; i < sourcePixels.length; i++) {
            int srcPixel = sourcePixels[i];
            int dstPixel = destinationPixel[i];

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
            // Wir nutzen "Screen" (Negativ Multiplizieren) für farbige Bereiche,
            // um weiche Verläufe ohne abruptes Ausbrennen zu erzielen.
            int screenR = 255 - ((255 - rDst) * (255 - dimmedRSrc) / 255);
            int screenG = 255 - ((255 - gDst) * (255 - dimmedGSrc) / 255);
            int screenB = 255 - ((255 - bDst) * (255 - dimmedBSrc) / 255);

            // Dynamischer Mix: Je gesättigter die Lichtquelle, desto mehr Screen nutzen wir.
            // Das bewahrt die lebendigen Farben (z.B. Lila bei Rot + Blau).
            // Ungesättigtes (weißes) Licht bleibt knackig additiv.
            int finalR = (int) (addR + saturation * (screenR - addR));
            int finalG = (int) (addG + saturation * (screenG - addG));
            int finalB = (int) (addB + saturation * (screenB - addB));

            // Letzte Absicherung gegen Überläufe
            finalR = Math.max(0, Math.min(255, finalR));
            finalG = Math.max(0, Math.min(255, finalG));
            finalB = Math.max(0, Math.min(255, finalB));

            // Ziel-Alpha berechnen (Standard-Lichtmischung behält Ziel-Alpha oder addiert Anteile)
            int finalA = Math.max(aDst, (int)(aSrc * extraAlpha));

            destinationPixel[i] = (finalA << 24) | (finalR << 16) | (finalG << 8) | finalB;
        }

        dstOut.setDataElements(0, 0, width, height, destinationPixel);
    }
}