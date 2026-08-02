package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.smoke.SmokeStyle;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class SmokeRenderer {
    //TODO reuse bufferimage
    //TODO only switch grid size when resolution changes
    //TODO only create image from visible cells
    //TODO do not render image when empty
    public BufferedImage createImage(final int scale, final SmokeStyle style, final DensityData state, final ScreenBounds visibleBounds) {
        int totalCells = state.cells(); // Gesamtzahl der Zellen im Quellgitter

        // Extrahiere Subimage-Dimensionen in Zellen (Ausschnitt aus dem globalen Gitter)
        int startX = visibleBounds.x();
        int startY = visibleBounds.y();
        int viewWidthCells = visibleBounds.width();
        int viewHeightCells = visibleBounds.height();

        // Zielgröße des neuen Bildes in Pixeln
        int targetWidth = viewWidthCells * scale;
        int targetHeight = viewHeightCells * scale;

        // Erstelle das Bild exakt in der benötigten Zielgröße (nicht mehr quadratisch blockiert)
        Size size = Size.of(targetWidth, targetHeight);
        var image = ImageOperations.createImage(size);
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        // 1. Look-Up-Tabellen (LUT) für X-Achse vorbereiten (relativ zu startX)
        int[] x0Arr = new int[targetWidth];
        int[] x1Arr = new int[targetWidth];
        float[] tXArr = new float[targetWidth];

        for (int x = 0; x < targetWidth; x++) {
            // Berechne die Fließkomma-Zellposition innerhalb des Subimages und addiere den globalen Startversatz
            float srcX = startX + ((float) x / scale);
            int x0 = (int) srcX;

            x0Arr[x] = Math.clamp(x0, 0, totalCells - 1);
            x1Arr[x] = Math.clamp(x0 + 1L, 0, totalCells - 1);
            tXArr[x] = srcX - x0;
        }

        // 2. Hauptschleife mit optimierter Interpolation über die Subimage-Pixel
        for (int y = 0; y < targetHeight; y++) {
            int pixelIndex = y * targetWidth;

            // Berechne die Fließkomma-Zellposition innerhalb des Subimages und addiere den globalen Startversatz
            float srcY = startY + ((float) y / scale);
            int y0 = (int) srcY;

            int clampedY0 = Math.clamp(y0, 0, totalCells - 1);
            int clampedY1 = Math.clamp(y0 + 1L, 0, totalCells - 1);
            float tY = srcY - y0;
            float invTY = 1.0f - tY;

            for (int x = 0; x < targetWidth; x++) {
                int x0 = x0Arr[x];
                int x1 = x1Arr[x];
                float tX = tXArr[x];
                float invTX = 1.0f - tX;

                // Gewichtungen vorab berechnen
                float w00 = invTX * invTY;
                float w10 = tX * invTY;
                float w01 = invTX * tY;
                float w11 = tX * tY;

                int index1 = state.calculateIndex(x0, clampedY0);
                int index2 = state.calculateIndex(x1, clampedY0);
                int index3 = state.calculateIndex(x0, clampedY1);
                int index4 = state.calculateIndex(x1, clampedY1);
                final float r = Math.clamp((float) (state.red(index1) * w00 +
                                                    state.red(index2) * w10 +
                                                    state.red(index3) * w01 +
                                                    state.red(index4) * w11), 0.0f, 1.0f);
                final float g = Math.clamp((float) (state.green(index1) * w00 +
                                                    state.green(index2) * w10 +
                                                    state.green(index3) * w01 +
                                                    state.green(index4) * w11), 0.0f, 1.0f);
                final float b = Math.clamp((float) (state.blue(index1) * w00 +
                                                    state.blue(index2) * w10 +
                                                    state.blue(index3) * w01 +
                                                    state.blue(index4) * w11), 0.0f, 1.0f);

                final float a = Math.clamp((float) (state.alpha(index1) * w00 +
                                                    state.alpha(index2) * w10 +
                                                    state.alpha(index3) * w01 +
                                                    state.alpha(index4) * w11), 0.0f, 1.0f);

                pixels[pixelIndex + x] = style.apply(r, g, b, a);
            }
        }
        return image;
    }
}
