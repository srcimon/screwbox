package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.smoke.SmokeStyle;

import java.awt.image.DataBufferInt;

public class SmokeRenderer {
    //TODO reuse bufferimage
    //TODO only switch grid size when resolution changes
    //TODO only create image from visible cells
    //TODO do not render image when empty
    public Sprite createImage(GraphicsConfiguration graphicsConfiguration, FluidSimulationState state, ScreenBounds actuallyVisibleBounds) {
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
                                                    state.densityAlpha(index4) * w11), 0.0f, 1.0f);

// 2. Alpha direkt aus der Dichte/Helligkeit bestimmen (0.0 - 1.0)


// 4. Direktes Schreiben ohne Maskierungs-Fehler
                pixels[pixelIndex + x] = DEFAULT.apply(r, g, b, a);


            }
        }

        if (graphicsConfiguration.smokeBlur() > 0) {
            ImageOperations.blurImage(image, graphicsConfiguration.smokeBlur());
        }

        return Sprite.fromImage(image);
    }

    public static final SmokeStyle FROZEN_FROST = (r, g, b, a) -> {
        float density = a;

        // Schwellenwert: Sehr dünner Rauch wird weggeschnitten für kristalline Strukturen
        if (density < 0.15f) {
            return 0;
        }

        // Standard-Eisblau-Palette basierend auf der Dichte
        float funR = Math.clamp(density * 0.4f, 0.0f, 1.0f);
        float funG = Math.clamp(0.4f + density * 0.5f, 0.0f, 1.0f);
        float funB = Math.clamp(0.7f + density * 0.3f, 0.0f, 1.0f);

        // Je dichter der Rauch, desto strahlender (weißer) wird der Kern
        if (density > 0.7f) {
            funR = Math.clamp(funR + (density - 0.7f) * 2.0f, 0.0f, 1.0f);
            funG = Math.clamp(funG + (density - 0.7f) * 2.0f, 0.0f, 1.0f);
        }

        int rPremult = (int) (funR * a * 255.0f + 0.5f);
        int gPremult = (int) (funG * a * 255.0f + 0.5f);
        int bPremult = (int) (funB * a * 255.0f + 0.5f);
        int aInt = (int) (a * 255.0f + 0.5f);
        return (aInt << 24) | (rPremult << 16) | (gPremult << 8) | bPremult;
    };
    public static final SmokeStyle DEFAULT = (r, g, b, a) -> {
        int rPremult = (int) (r * a * 255.0f + 0.5f);
        int gPremult = (int) (g * a * 255.0f + 0.5f);
        int bPremult = (int) (b * a * 255.0f + 0.5f);
        int aInt = (int) (a * 255.0f + 0.5f);
        return (aInt << 24) | (rPremult << 16) | (gPremult << 8) | bPremult;
    };

    public static final SmokeStyle FIRE = (r, g, b, a) -> {
        float intensity = (r + g + b) / 3.0f * (a );
        float funR = Math.clamp(intensity * 2.5f, 0.0f, 1.0f);
        float funG = Math.clamp((intensity - 0.3f) * 2.0f, 0.0f, 1.0f);
        float funB = Math.clamp((intensity - 0.7f) * 4.0f, 0.0f, 1.0f);
        float funA = Math.max(intensity * 1.5f, 0.0f);

        int rPremult = (int) (funR * funA * 255.0f + 0.5f);
        int gPremult = (int) (funG * funA * 255.0f + 0.5f);
        int bPremult = (int) (funB * funA * 255.0f + 0.5f);
        int aInt = (int) (funA * 255.0f + 0.5f);
        return (aInt << 24) | (rPremult << 16) | (gPremult << 8) | bPremult;
    };

    public static final SmokeStyle HEAT_VISION = (r, g, b, a) -> {
        float funR = (float) Math.sin((a) * 2.0 * Math.PI + 0.0) * 0.5f + 0.5f;
        float funG = (float) Math.sin((a) * 2.0 * Math.PI + 2.094) * 0.5f + 0.5f;
        float funB = (float) Math.sin((a) * 2.0 * Math.PI + 4.188) * 0.5f + 0.5f;

        int rPremult = (int) (funR * a * 255.0f + 0.5f);
        int gPremult = (int) (funG * a * 255.0f + 0.5f);
        int bPremult = (int) (funB * a * 255.0f + 0.5f);
        int aInt = (int) (a * 255.0f + 0.5f);
        return (aInt << 24) | (rPremult << 16) | (gPremult << 8) | bPremult;
    };

    public static final SmokeStyle COMIC = (r, g, b, a) -> {
        float maxChannel = Math.max(r, Math.max(g, b));
        float funR = (r == maxChannel) ? Math.clamp(r * 1.8f, 0.0f, 1.0f) : r * 0.3f;
        float funG = (g == maxChannel) ? Math.clamp(g * 1.8f, 0.0f, 1.0f) : g * 0.3f;
        float funB = (b == maxChannel) ? Math.clamp(b * 1.8f, 0.0f, 1.0f) : b * 0.3f;

        if (maxChannel > 0.8f) {
            funR = Math.clamp(funR + 0.2f, 0.0f, 1.0f);
            funG = Math.clamp(funG + 0.2f, 0.0f, 1.0f);
            funB = Math.clamp(funB + 0.2f, 0.0f, 1.0f);
        }

        funR = Math.round(funR * 2.0f) / 2.0f;
        funG = Math.round(funG * 2.0f) / 2.0f;
        funB = Math.round(funB * 2.0f) / 2.0f;

        int rPremult = (int) (funR * a * 255.0f + 0.5f);
        int gPremult = (int) (funG * a * 255.0f + 0.5f);
        int bPremult = (int) (funB * a * 255.0f + 0.5f);
        int aInt = (int) (a * 255.0f + 0.5f);
        return (aInt << 24) | (rPremult << 16) | (gPremult << 8) | bPremult;
    };
}
