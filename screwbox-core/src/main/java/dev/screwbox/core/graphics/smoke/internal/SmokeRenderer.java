package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.smoke.SmokeStyle;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class SmokeRenderer {

    private BufferedImage image;
    private Size imageSize;

    public BufferedImage renderSmoke(final DensityInfo densityInfo, final ScreenBounds bounds, final int scale, final SmokeStyle style) {
        final int startX = bounds.x();
        final int startY = bounds.y();
        final int targetWidth = bounds.width() * scale;
        final int targetHeight = bounds.height() * scale;

        final Size size = Size.of(targetWidth, targetHeight);
        if (!size.equals(imageSize)) {
            image = ImageOperations.createImage(size);
            imageSize = size;
        }
        final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        final int[] x0Arr = new int[targetWidth];
        final int[] x1Arr = new int[targetWidth];
        final float[] tXArr = new float[targetWidth];

        for (int x = 0; x < targetWidth; x++) {
            final float srcX = startX + ((float) x / scale);
            final int x0 = (int) srcX;

            x0Arr[x] = Math.clamp(x0, 0, densityInfo.cells() - 1);
            x1Arr[x] = Math.clamp(x0 + 1L, 0, densityInfo.cells() - 1);
            tXArr[x] = srcX - x0;
        }

        for (int y = 0; y < targetHeight; y++) {
            final int pixelIndex = y * targetWidth;

            final float srcY = startY + ((float) y / scale);
            final int y0 = (int) srcY;

            final int clampedY0 = Math.clamp(y0, 0, densityInfo.cells() - 1);
            final int clampedY1 = Math.clamp(y0 + 1L, 0, densityInfo.cells() - 1);
            final float tY = srcY - y0;
            final float invTY = 1.0f - tY;

            for (int x = 0; x < targetWidth; x++) {
                final float tX = tXArr[x];
                final float invTX = 1.0f - tX;

                final float w00 = invTX * invTY;
                final float w10 = tX * invTY;
                final float w01 = invTX * tY;
                final float w11 = tX * tY;

                final int index1 = densityInfo.calculateIndex(x0Arr[x], clampedY0);
                final int index2 = densityInfo.calculateIndex(x1Arr[x], clampedY0);
                final int index3 = densityInfo.calculateIndex(x0Arr[x], clampedY1);
                final int index4 = densityInfo.calculateIndex(x1Arr[x], clampedY1);

                final float r = clampRgb(densityInfo.red(index1) * w00 +
                                         densityInfo.red(index2) * w10 +
                                         densityInfo.red(index3) * w01 +
                                         densityInfo.red(index4) * w11);

                final float g = clampRgb(densityInfo.green(index1) * w00 +
                                         densityInfo.green(index2) * w10 +
                                         densityInfo.green(index3) * w01 +
                                         densityInfo.green(index4) * w11);

                final float b = clampRgb(densityInfo.blue(index1) * w00 +
                                         densityInfo.blue(index2) * w10 +
                                         densityInfo.blue(index3) * w01 +
                                         densityInfo.blue(index4) * w11);

                final float a = clampRgb(densityInfo.alpha(index1) * w00 +
                                         densityInfo.alpha(index2) * w10 +
                                         densityInfo.alpha(index3) * w01 +
                                         densityInfo.alpha(index4) * w11);

                pixels[pixelIndex + x] = style.apply(r, g, b, a);
            }
        }
        return image;
    }

    private static float clampRgb(final double value) {
        return Math.clamp((float) value, 0.0f, 1.0f);
    }
}
