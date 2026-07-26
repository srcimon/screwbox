package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.internal.ImageOperations;

import java.awt.image.DataBufferInt;

public class SmokeRenderer {
    //TODO reuse bufferimage
    //TODO only switch grid size when resolution changes
    //TODO only create image from visible cells
    //TODO do not render image when empty
    public Sprite createImage(int blur, int upscale, Percent maxOpacity, FluidSimulationState fluidSimulationState, ScreenBounds actuallyVisibleBounds) {
        int b1 = maxOpacity.rangeValue(0, 255);
        int totalCells = fluidSimulationState.cells(); // Gesamtzahl der Zellen im Quellgitter

        // Extrahiere Subimage-Dimensionen in Zellen (Ausschnitt aus dem globalen Gitter)
        int startX = actuallyVisibleBounds.x();
        int startY = actuallyVisibleBounds.y();
        int viewWidthCells = actuallyVisibleBounds.width();
        int viewHeightCells = actuallyVisibleBounds.height();

        // Zielgröße des neuen Bildes in Pixeln
        int targetWidth = viewWidthCells * upscale;
        int targetHeight = viewHeightCells * upscale;

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
            float srcX = startX + ((float) x / upscale);
            int x0 = (int) srcX;

            x0Arr[x] = Math.clamp(x0, 0, totalCells - 1);
            x1Arr[x] = Math.clamp(x0 + 1, 0, totalCells - 1);
            tXArr[x] = srcX - x0;
        }

        // 2. Hauptschleife mit optimierter Interpolation über die Subimage-Pixel
        for (int y = 0; y < targetHeight; y++) {
            int pixelIndex = y * targetWidth;

            // Berechne die Fließkomma-Zellposition innerhalb des Subimages und addiere den globalen Startversatz
            float srcY = startY + ((float) y / upscale);
            int y0 = (int) srcY;

            int clampedY0 = Math.clamp(y0, 0, totalCells - 1);
            int clampedY1 = Math.clamp(y0 + 1, 0, totalCells - 1);
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

                // 1. Clamping der float-Werte direkt auf 0.0 - 1.0
                final float r = Math.clamp((float) (fluidSimulationState.densityRed(x0, clampedY0) * w00 +
                                                    fluidSimulationState.densityRed(x1, clampedY0) * w10 +
                                                    fluidSimulationState.densityRed(x0, clampedY1) * w01 +
                                                    fluidSimulationState.densityRed(x1, clampedY1) * w11), 0.0f, 1.0f);
                final float g = Math.clamp((float) (fluidSimulationState.densityGreen(x0, clampedY0) * w00 +
                                                    fluidSimulationState.densityGreen(x1, clampedY0) * w10 +
                                                    fluidSimulationState.densityGreen(x0, clampedY1) * w01 +
                                                    fluidSimulationState.densityGreen(x1, clampedY1) * w11), 0.0f, 1.0f);
                final float b = Math.clamp((float) (fluidSimulationState.densityBlue(x0, clampedY0) * w00 +
                                                    fluidSimulationState.densityBlue(x1, clampedY0) * w10 +
                                                    fluidSimulationState.densityBlue(x0, clampedY1) * w01 +
                                                    fluidSimulationState.densityBlue(x1, clampedY1) * w11), 0.0f, 1.0f);

// 2. Alpha direkt aus der Dichte/Helligkeit bestimmen (0.0 - 1.0)
                float maxChannel = Math.max(r, Math.max(g, b));
                float alpha = Math.min(maxChannel, b1 / 255.0f); // b1 muss normalisiert werden, falls es 0-255 ist

// 3. Premultiplied Alpha direkt im Float-Raum berechnen
                int rPremult = (int) (r * alpha * 255.0f + 0.5f);
                int gPremult = (int) (g * alpha * 255.0f + 0.5f);
                int bPremult = (int) (b * alpha * 255.0f + 0.5f);
                int aInt = (int) (alpha * 255.0f + 0.5f);

// 4. Direktes Schreiben ohne Maskierungs-Fehler
                pixels[pixelIndex + x] = (aInt << 24) | (rPremult << 16) | (gPremult << 8) | bPremult;


            }
        }

        if (blur > 0) {
            ImageOperations.blurImage(image, blur);
        }

        return Sprite.fromImage(image);
    }
}
