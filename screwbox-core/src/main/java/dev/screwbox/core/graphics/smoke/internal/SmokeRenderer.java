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
        float b1 = (float)maxOpacity.value();
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
            x1Arr[x] = Math.clamp(x0 + 1L, 0, totalCells - 1);
            tXArr[x] = srcX - x0;
        }

        // 2. Hauptschleife mit optimierter Interpolation über die Subimage-Pixel
        for (int y = 0; y < targetHeight; y++) {
            int pixelIndex = y * targetWidth;

            // Berechne die Fließkomma-Zellposition innerhalb des Subimages und addiere den globalen Startversatz
            float srcY = startY + ((float) y / upscale);
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

                final float a = Math.clamp((float) (fluidSimulationState.densityAlpha(x0, clampedY0) * w00 +
                                                    fluidSimulationState.densityAlpha(x1, clampedY0) * w10 +
                                                    fluidSimulationState.densityAlpha(x0, clampedY1) * w01 +
                                                    fluidSimulationState.densityAlpha(x1, clampedY1) * w11), 0.0f, b1);

// 2. Alpha direkt aus der Dichte/Helligkeit bestimmen (0.0 - 1.0)

// 3. Premultiplied Alpha direkt im Float-Raum berechnen
                int rPremult = (int) (r * a * 255.0f + 0.5f);
                int gPremult = (int) (g * a * 255.0f + 0.5f);
                int bPremult = (int) (b * a * 255.0f + 0.5f);
                int aInt = (int) (a * 255.0f + 0.5f);

// 4. Direktes Schreiben ohne Maskierungs-Fehler
                pixels[pixelIndex + x] = (aInt << 24) | (rPremult << 16) | (gPremult << 8) | bPremult;


            }
        }

        if (blur > 0) {
            ImageOperations.blurImage(image, blur);
        }

        return Sprite.fromImage(image);
    }


    //TODO fire renderer
//    Keep your original r, g, b, a interpolation code above...
//
//    // Calculate a brightness/heat intensity based on density and alpha
//    float intensity = (r + g + b) / 3.0f * (a / b1);
//
//    // Map intensity to a fire gradient
//    float funR = Math.clamp(intensity * 2.5f, 0.0f, 1.0f);        // Red shows up early
//    float funG = Math.clamp((intensity - 0.3f) * 2.0f, 0.0f, 1.0f); // Green comes in for orange/yellow
//    float funB = Math.clamp((intensity - 0.7f) * 4.0f, 0.0f, 1.0f); // Blue appears only at the hottest core
//
//    // Boost alpha for a more energetic glow effect
//    float funA = Math.clamp(intensity * 1.5f, 0.0f, b1);
//
//    int rPremult = (int) (funR * funA * 255.0f + 0.5f);
//    int gPremult = (int) (funG * funA * 255.0f + 0.5f);
//    int bPremult = (int) (funB * funA * 255.0f + 0.5f);
//    int aInt = (int) (funA * 255.0f + 0.5f);
//
//    pixels[pixelIndex + x] = (aInt << 24) | (rPremult << 16) | (gPremult << 8) | bPremult;

    //TODO night vision renderer
//    // Keep your original r, g, b, a interpolation code above...
//
//    // Use the current alpha or density as a hue shifter
//    float hue = (a / b1);
//
//    // Simple procedural RGB rainbow wheel
//    float funR = (float) Math.sin(hue * 2.0 * Math.PI + 0.0) * 0.5f + 0.5f;
//    float funG = (float) Math.sin(hue * 2.0 * Math.PI + 2.094) * 0.5f + 0.5f; // +120 degrees
//    float funB = (float) Math.sin(hue * 2.0 * Math.PI + 4.188) * 0.5f + 0.5f; // +240 degrees
//
//    int rPremult = (int) (funR * a * 255.0f + 0.5f);
//    int gPremult = (int) (funG * a * 255.0f + 0.5f);
//    int bPremult = (int) (funB * a * 255.0f + 0.5f);
//    int aInt = (int) (a * 255.0f + 0.5f);
//
//    pixels[pixelIndex + x] = (aInt << 24) | (rPremult << 16) | (gPremult << 8) | bPremult;
}
