package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.smoke.SmokeStyle;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class SmokeRenderer {

    public BufferedImage renderSmoke(final DensityData state, final ScreenBounds bounds, final int scale, final SmokeStyle style) {
        final int startX = bounds.x();
        final int startY = bounds.y();
        final int targetWidth = bounds.width() * scale;
        final int targetHeight = bounds.height() * scale;

        // Erstelle das Bild exakt in der benötigten Zielgröße (nicht mehr quadratisch blockiert)
        final Size size = Size.of(targetWidth, targetHeight);
        final var image = ImageOperations.createImage(size);
        final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        // 1. Look-Up-Tabellen (LUT) für X-Achse vorbereiten (relativ zu startX)
        int[] x0Arr = new int[targetWidth];
        int[] x1Arr = new int[targetWidth];
        float[] tXArr = new float[targetWidth];

        for (int x = 0; x < targetWidth; x++) {
            float srcX = startX + ((float) x / scale);
            int x0 = (int) srcX;

            x0Arr[x] = Math.clamp(x0, 0, state.cells() - 1);
            x1Arr[x] = Math.clamp(x0 + 1L, 0, state.cells() - 1);
            tXArr[x] = srcX - x0;
        }

        for (int y = 0; y < targetHeight; y++) {
            int pixelIndex = y * targetWidth;

            float srcY = startY + ((float) y / scale);
            int y0 = (int) srcY;

            int clampedY0 = Math.clamp(y0, 0, state.cells() - 1);
            int clampedY1 = Math.clamp(y0 + 1L, 0, state.cells() - 1);
            float tY = srcY - y0;
            float invTY = 1.0f - tY;

            for (int x = 0; x < targetWidth; x++) {
                float tX = tXArr[x];
                float invTX = 1.0f - tX;

                // Gewichtungen vorab berechnen
                float w00 = invTX * invTY;
                float w10 = tX * invTY;
                float w01 = invTX * tY;
                float w11 = tX * tY;

                final int index1 = state.calculateIndex(x0Arr[x], clampedY0);
                final int index2 = state.calculateIndex(x1Arr[x], clampedY0);
                final int index3 = state.calculateIndex(x0Arr[x], clampedY1);
                final int index4 = state.calculateIndex(x1Arr[x], clampedY1);
                final float r = clampRgb(state.red(index1) * w00 +
                                         state.red(index2) * w10 +
                                         state.red(index3) * w01 +
                                         state.red(index4) * w11);
                final float g = clampRgb(state.green(index1) * w00 +
                                         state.green(index2) * w10 +
                                         state.green(index3) * w01 +
                                         state.green(index4) * w11);
                final float b = clampRgb(state.blue(index1) * w00 +
                                         state.blue(index2) * w10 +
                                         state.blue(index3) * w01 +
                                         state.blue(index4) * w11);

                final float a = clampRgb(state.alpha(index1) * w00 +
                                         state.alpha(index2) * w10 +
                                         state.alpha(index3) * w01 +
                                         state.alpha(index4) * w11);

                pixels[pixelIndex + x] = style.apply(r, g, b, a);
            }
        }
        return image;
    }

    private static float clampRgb(final double value) {
        return Math.clamp((float) value, 0.0f, 1.0f);
    }
}
