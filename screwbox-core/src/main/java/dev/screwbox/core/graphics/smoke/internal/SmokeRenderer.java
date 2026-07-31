package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.graphics.GraphicsConfiguration;
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
    public Sprite createImage(GraphicsConfiguration graphicsConfiguration, FluidSimulationState state, ScreenBounds actuallyVisibleBounds) {
        float b1 = (float) graphicsConfiguration.smokeOpacity().value();
        int totalCells = state.cells(); // Gesamtzahl der Zellen im Quellgitter

        // Extrahiere Subimage-Dimensionen in Zellen (Ausschnitt aus dem globalen Gitter)
        int startX = actuallyVisibleBounds.x();
        int startY = actuallyVisibleBounds.y();
        int viewWidthCells = actuallyVisibleBounds.width();
        int viewHeightCells = actuallyVisibleBounds.height();

        // Zielgröße des neuen Bildes in Pixeln
        int targetWidth = viewWidthCells * graphicsConfiguration.smokeScale();
        int targetHeight = viewHeightCells * graphicsConfiguration.smokeScale();

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
            float srcX = startX + ((float) x / graphicsConfiguration.smokeScale());
            int x0 = (int) srcX;

            x0Arr[x] = Math.clamp(x0, 0, totalCells - 1);
            x1Arr[x] = Math.clamp(x0 + 1L, 0, totalCells - 1);
            tXArr[x] = srcX - x0;
        }

        // 2. Hauptschleife mit optimierter Interpolation über die Subimage-Pixel
        for (int y = 0; y < targetHeight; y++) {
            int pixelIndex = y * targetWidth;

            // Berechne die Fließkomma-Zellposition innerhalb des Subimages und addiere den globalen Startversatz
            float srcY = startY + ((float) y / graphicsConfiguration.smokeScale());
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
                final float r = Math.clamp((float) (state.densityRed(index1) * w00 +
                                                    state.densityRed(index2) * w10 +
                                                    state.densityRed(index3) * w01 +
                                                    state.densityRed(index4) * w11), 0.0f, 1.0f);
                final float g = Math.clamp((float) (state.densityGreen(index1) * w00 +
                                                    state.densityGreen(index2) * w10 +
                                                    state.densityGreen(index3) * w01 +
                                                    state.densityGreen(index4) * w11), 0.0f, 1.0f);
                final float b = Math.clamp((float) (state.densityBlue(index1) * w00 +
                                                    state.densityBlue(index2) * w10 +
                                                    state.densityBlue(index3) * w01 +
                                                    state.densityBlue(index4) * w11), 0.0f, 1.0f);

                final float a = Math.clamp((float) (state.densityAlpha(index1) * w00 +
                                                    state.densityAlpha(index2) * w10 +
                                                    state.densityAlpha(index3) * w01 +
                                                    state.densityAlpha(index4) * w11), 0.0f, b1);

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

        if (graphicsConfiguration.smokeBlur() > 0) {
            ImageOperations.blurImage(image, graphicsConfiguration.smokeBlur());
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

    //TODO comic look
//    float maxChannel = Math.max(r, Math.max(g, b));
//
//    // 1. Aggressively boost the dominant channel, dim the weaker ones
//    float funR = (r == maxChannel) ? Math.clamp(r * 1.8f, 0.0f, 1.0f) : r * 0.3f;
//    float funG = (g == maxChannel) ? Math.clamp(g * 1.8f, 0.0f, 1.0f) : g * 0.3f;
//    float funB = (b == maxChannel) ? Math.clamp(b * 1.8f, 0.0f, 1.0f) : b * 0.3f;
//
//// 2. Add a solid glowing core for high concentration areas
//if (maxChannel > 0.8f) {
//        funR = Math.clamp(funR + 0.2f, 0.0f, 1.0f);
//        funG = Math.clamp(funG + 0.2f, 0.0f, 1.0f);
//        funB = Math.clamp(funB + 0.2f, 0.0f, 1.0f);
//    }
//
//// 3. COMICAL EFFECT: Posterize colors into 3 distinct, flat steps
//    funR = Math.round(funR * 2.0f) / 2.0f;
//    funG = Math.round(funG * 2.0f) / 2.0f;
//    funB = Math.round(funB * 2.0f) / 2.0f;
//
//    // 4. COMICAL EFFECT: Flatten alpha into sharp steps and clear thin edges
//    float funA = a;
//if (funA < 0.15f * b1) {
//        funA = 0.0f;             // Sharp cut-off at the edges (no transparent fuzz)
//    } else if (funA < 0.6f * b1) {
//        funA = 0.5f * b1;        // Semi-transparent outer puff
//    } else {
//        funA = b1;               // Fully solid cartoon core
//    }
//
//    // 5. Premultiplied Alpha calculation using the new comic colors & alpha
//    int rPremult = (int) (funR * funA * 255.0f + 0.5f);
//    int gPremult = (int) (funG * funA * 255.0f + 0.5f);
//    int bPremult = (int) (funB * funA * 255.0f + 0.5f);
//    int aInt = (int) (funA * 255.0f + 0.5f);
//
//// 6. Write to pixel array
//    pixels[pixelIndex + x] = (aInt << 24) | (rPremult << 16) | (gPremult << 8) | bPremult;
}
