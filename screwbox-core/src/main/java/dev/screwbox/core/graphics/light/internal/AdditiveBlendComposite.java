package dev.screwbox.core.graphics.light.internal;

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class AdditiveBlendComposite implements Composite, CompositeContext {

    private final float extraAlpha;

    // Privater Konstruktor: Instanziierung nur über getInstance()
    private AdditiveBlendComposite(float alpha) {
        this.extraAlpha = Math.min(1.0f, Math.max(0.0f, alpha));
    }

    /**
     * Erstellt oder holt eine Instanz des additiven Composites mit definierter Opazität.
     * @param alpha Wert von 0.0f (voll transparent) bis 1.0f (voll deckend)
     */
    public static AdditiveBlendComposite getInstance(float alpha) {
        // Für maximale Performance könnte man hier häufig genutzte Werte cachen,
        // aber das Erstellen des Contexts ist in Java 2D bereits sehr leichtgewichtig.
        return new AdditiveBlendComposite(alpha);
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

            // ========================================================
            // KORREKTUR: Den effektiven Alpha-Faktor für DIESES Pixel berechnen
            // Er setzt sich aus dem Pixel-Alpha (vom Verlauf) und der globalen Opazität zusammen.
            // ========================================================
            float effectiveAlpha = (aSrc / 255.0f) * extraAlpha;

            // Die RGB-Farbe des neuen Lichts MUSS mit diesem Faktor gedimmt werden,
            // da sie sonst mit voller Wucht aufaddiert wird, egal wie transparent das Licht ist.
            int dimmedRSrc = (int) (rSrc * effectiveAlpha);
            int dimmedGSrc = (int) (gSrc * effectiveAlpha);
            int dimmedBSrc = (int) (bSrc * effectiveAlpha);
            int dimmedASrc = (int) (aSrc * extraAlpha); // Für die Alpha-Addition

            // 2. Ziel-Pixel (Bereits vorhandene Lichter auf dem Overlay) extrahieren
            int aDst = (dstPixel >> 24) & 0xFF;
            int rDst = (dstPixel >> 16) & 0xFF;
            int gDst = (dstPixel >> 8) & 0xFF;
            int bDst = dstPixel & 0xFF;

            // 3. Gedimmtes neues Licht auf das alte Licht aufaddieren (Additive / Linear Dodge)
            int rFinal = Math.min(255, dimmedRSrc + rDst);
            int gFinal = Math.min(255, dimmedGSrc + gDst);
            int bFinal = Math.min(255, dimmedBSrc + bDst);
            int aFinal = Math.min(255, dimmedASrc + aDst);

            dstPixels[i] = (aFinal << 24) | (rFinal << 16) | (gFinal << 8) | bFinal;
        }

        dstOut.setDataElements(0, 0, w, h, dstPixels);
    }
}