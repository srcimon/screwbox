package dev.screwbox.core.graphics.internal;

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class MaxAlphaComposite implements Composite, CompositeContext {

    private final ColorModel srcColorModel;
    private final ColorModel dstColorModel;

    public MaxAlphaComposite() {
        this(null, null);
    }

    private MaxAlphaComposite(final ColorModel srcColorModel, final ColorModel dstColorModel) {
        this.srcColorModel = srcColorModel;
        this.dstColorModel = dstColorModel;
    }

    @Override
    public CompositeContext createContext(final ColorModel srcColorModel, final ColorModel dstColorModel, final RenderingHints hints) {
        return new MaxAlphaComposite(srcColorModel, dstColorModel);
    }

    @Override
    public void dispose() {
        // no resources allocated
    }

    @Override
    public void compose(final Raster src, final Raster dstIn, final WritableRaster dstOut) {
        final int width = Math.min(src.getWidth(), dstIn.getWidth());
        final int height = Math.min(src.getHeight(), dstIn.getHeight());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final int srcX = x + src.getMinX();
                final int srcY = y + src.getMinY();
                final int dstX = x + dstIn.getMinX();
                final int dstY = y + dstIn.getMinY();

                final Object srcData = src.getDataElements(srcX, srcY, null);
                final Object dstData = dstIn.getDataElements(dstX, dstY, null);

                final int srcA = srcColorModel.getAlpha(srcData);
                final int dstA = dstColorModel.getAlpha(dstData);

                dstOut.setDataElements(dstX, dstY, srcA > dstA ? srcData : dstData);
            }
        }
    }
}
