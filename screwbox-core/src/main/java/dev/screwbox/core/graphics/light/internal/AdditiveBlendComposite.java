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

            // 1. Quell-Pixel (Neues Licht) extrahieren
            int aSrc = (srcPixel >> 24) & 0xFF;
            int rSrc = (srcPixel >> 16) & 0xFF;
            int gSrc = (srcPixel >> 8) & 0xFF;
            int bSrc = srcPixel & 0xFF;

            // Berechne die tatsächliche Stärke dieses Pixels (Verlauf * globale Opazität)
            float factor = (aSrc / 255.0f) * extraAlpha;

            // 2. Ziel-Pixel (Bestehende Lichter auf dem Overlay) extrahieren
            int aDst = (dstPixel >> 24) & 0xFF;
            int rDst = (dstPixel >> 16) & 0xFF;
            int gDst = (dstPixel >> 8) & 0xFF;
            int bDst = dstPixel & 0xFF;

            // 3. ECHTE FARBMISCHUNG (Screen-Blend-Verfahren)
            // Verhindert das "Auskalken" zu schnellem Weiß und mischt RGB-Kanäle sauber.
            // Formel: Src + Dst - (Src * Dst / 255)
            int rMixed = (int) (rSrc + rDst - ((rSrc * rDst) / 255.0f));
            int gMixed = (int) (gSrc + gDst - ((gSrc * gDst) / 255.0f));
            int bMixed = (int) (bSrc + bDst - ((bSrc * bDst) / 255.0f));
            int aMixed = (int) (aSrc + aDst - ((aSrc * aDst) / 255.0f));

            // 4. Opazitäts-Dimmfaktor (factor) auf die Mischung anwenden
            // Wenn factor = 0 (Licht unsichtbar), bleibt das alte Pixel (Dst) unverändert.
            // Wenn factor = 1 (Licht voll da), nehmen wir das gemischte Ergebnis.
            int rFinal = (int) (rDst * (1.0f - factor) + rMixed * factor);
            int gFinal = (int) (gDst * (1.0f - factor) + gMixed * factor);
            int bFinal = (int) (bDst * (1.0f - factor) + bMixed * factor);
            int aFinal = (int) (aDst * (1.0f - factor) + aMixed * factor);

            // Sicherheits-Clipping gegen Rundungsfehler
            rFinal = Math.min(255, Math.max(0, rFinal));
            gFinal = Math.min(255, Math.max(0, gFinal));
            bFinal = Math.min(255, Math.max(0, bFinal));
            aFinal = Math.min(255, Math.max(0, aFinal));

            dstPixels[i] = (aFinal << 24) | (rFinal << 16) | (gFinal << 8) | bFinal;
        }

        dstOut.setDataElements(0, 0, w, h, dstPixels);
    }
}