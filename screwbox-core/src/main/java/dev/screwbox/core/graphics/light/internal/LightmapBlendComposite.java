package dev.screwbox.core.graphics.light.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.utils.MathUtil;

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * Tries to preserve colors to improve visuals of merged colored light sources.
 */
public class LightmapBlendComposite implements Composite, CompositeContext {

    private final float extraAlpha;

    public LightmapBlendComposite(final Percent alpha) {
        this.extraAlpha = (float) alpha.value();
    }

    @Override
    public CompositeContext createContext(final ColorModel srcColorModel, final ColorModel dstColorModel, final RenderingHints hints) {
        return this;
    }

    @Override
    public void dispose() {
        // NOP
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
        final int gDst = Color.greenValue(dstPixel);
        final int bDst = Color.blueValue(dstPixel);

        final float factor = (aSrc / 255.0f) * extraAlpha;

        final int dimmedRSrc = (int) (rSrc * factor);
        final int dimmedGSrc = (int) (gSrc * factor);
        final int dimmedBSrc = (int) (bSrc * factor);

        final int maxColor = MathUtil.max(rSrc, gSrc, bSrc);
        final int minColor = MathUtil.min(rSrc, gSrc, bSrc);
        float saturation = (maxColor == 0) ? 0.0f : (float) (maxColor - minColor) / maxColor;

        final int addR = Math.min(255, rDst + dimmedRSrc);
        final int addG = Math.min(255, gDst + dimmedGSrc);
        final int addB = Math.min(255, bDst + dimmedBSrc);

        final int screenR = 255 - ((255 - rDst) * (255 - dimmedRSrc) / 255);
        final int screenG = 255 - ((255 - gDst) * (255 - dimmedGSrc) / 255);
        final int screenB = 255 - ((255 - bDst) * (255 - dimmedBSrc) / 255);

        final int r = Color.clampRgbRange((int) (addR + saturation * (screenR - addR)));
        final int g = Color.clampRgbRange((int) (addG + saturation * (screenG - addG)));
        final int b = Color.clampRgbRange((int) (addB + saturation * (screenB - addB)));

        final int alpha = Math.max(aDst, (int) (aSrc * extraAlpha));

        return (alpha << 24) | (r << 16) | (g << 8) | b;
    }
}