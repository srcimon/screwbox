package dev.screwbox.core.graphics.internal;

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class MaxAlphaComposite implements Composite, CompositeContext {

    @Override
    public CompositeContext createContext(final ColorModel srcColorModel, final ColorModel dstColorModel, final RenderingHints hints) {
        return this;
    }

    @Override
    public void dispose() {
        // no resources to dispose
    }

    @Override
    public void compose(final Raster src, final Raster dstIn, final WritableRaster dstOut) {
        final int width = Math.min(src.getWidth(), dstIn.getWidth());
        final int height = Math.min(src.getHeight(), dstIn.getHeight());

        final int[] srcPix = new int[src.getNumBands()];
        final int[] dstPix = new int[dstIn.getNumBands()];

        final int srcAlphaIdx = src.getNumBands() - 1;
        final int dstAlphaIdx = dstIn.getNumBands() - 1;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                src.getPixel(x + src.getMinX(), y + src.getMinY(), srcPix);
                dstIn.getPixel(x + dstIn.getMinX(), y + dstIn.getMinY(), dstPix);

                final int srcA = (src.getNumBands() > 3) ? srcPix[srcAlphaIdx] : 255;
                final int dstA = (dstIn.getNumBands() > 3) ? dstPix[dstAlphaIdx] : 255;

                var target = srcA > dstA ? srcPix : dstPix;
                dstOut.setPixel(x + dstOut.getMinX(), y + dstOut.getMinY(), target);
            }
        }
    }
}
