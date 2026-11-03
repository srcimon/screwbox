package dev.screwbox.core.graphics.light.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class AdditiveBlendComposite implements Composite, CompositeContext {

    private final float extraAlpha;

    // Privater Konstruktor: Instanziierung nur über getInstance()
    public AdditiveBlendComposite(final Percent alpha) {
        this.extraAlpha = (float) alpha.value();
    }


    @Override
    public CompositeContext createContext(final ColorModel srcColorModel, final ColorModel dstColorModel, final RenderingHints hints) {
        return this;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void compose(final Raster src, final Raster dstIn, final WritableRaster dstOut) {
        final int width = Math.min(src.getWidth(), dstIn.getWidth());
        final int height = Math.min(src.getHeight(), dstIn.getHeight());

        final int[] sourcePixels = new int[width * height];
        final int[] destinationPixel = new int[width * height];

        src.getDataElements(0, 0, width, height, sourcePixels);
        dstIn.getDataElements(0, 0, width, height, destinationPixel);

        for (int i = 0; i < sourcePixels.length; i++) {
            destinationPixel[i] = calculatePixelRgb(sourcePixels[i], destinationPixel[i]);
        }

        dstOut.setDataElements(0, 0, width, height, destinationPixel);
    }

    private int calculatePixelRgb(int srcPixel, int dstPixel) {
        final int aSrc = Color.alphaValue(srcPixel);
        final int rSrc = Color.redValue(srcPixel);
        final int gSrc = Color.greenValue(srcPixel);
        final int bSrc = Color.blueValue(srcPixel);

        final int aDst = Color.alphaValue(dstPixel);
        final int rDst = Color.redValue(dstPixel);
        final int gDst =  Color.greenValue(dstPixel);
        final int bDst = Color.blueValue(dstPixel);

        final float factor = (aSrc / 255.0f) * extraAlpha;

        // Gedimmte Werte der neuen Lichtquelle berechnen
        int dimmedRSrc = (int) (rSrc * factor);
        int dimmedGSrc = (int) (gSrc * factor);
        int dimmedBSrc = (int) (bSrc * factor);

        // Sättigung (Farbigkeit) des Quell-Lichts messen
        int maxColor = Math.max(rSrc, Math.max(gSrc, bSrc));
        int minColor = Math.min(rSrc, Math.min(gSrc, bSrc));
        float saturation = (maxColor == 0) ? 0.0f : (float) (maxColor - minColor) / maxColor;

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

        final int finalR = Color.clampRgbRange((int) (addR + saturation * (screenR - addR)));
        final int finalG = Color.clampRgbRange((int) (addG + saturation * (screenG - addG)));
        final int finalB = Color.clampRgbRange((int) (addB + saturation * (screenB - addB)));

        final int finalAlpha = Math.max(aDst, (int) (aSrc * extraAlpha));

        return (finalAlpha << 24) | (finalR << 16) | (finalG << 8) | finalB;
    }
}