package dev.screwbox.core.graphics.internal.filter;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

//TODO stack size increase the other way around
public class SizeIncreasingBlurImageFilter extends SizeIncreasingImageFilter {

    public SizeIncreasingBlurImageFilter(final int radius) {
        super(radius);
    }

    @Override
    public BufferedImage apply(final BufferedImage image) {
        final BufferedImage enlarged = super.apply(image);
        final int[] pixels = ((DataBufferInt) enlarged.getRaster().getDataBuffer()).getData();
        final int[] temp = new int[pixels.length];
        blurPassHorizontal(pixels, temp, enlarged.getWidth(), enlarged.getHeight(), radius);
        blurPassVertical(temp, pixels, enlarged.getHeight(), enlarged.getWidth(), radius);
        return enlarged;//TODO enlarge after blur
    }

    //TODO no more square scaling allows higher blur radius
    //TODO allow higher blur radius values
    //TODO document 28% speed increase on normal scale

    private static void blurPassHorizontal(int[] in, int[] out, int width, int height, int radius) {
        float scale = 1.0f / (radius * 2 + 1);

        for (int y = 0; y < height; y++) {

            float r = 0;
            float g = 0;
            float b = 0;
            float a = 0;

            // Initiales Fenster füllen
            for (int i = -radius; i <= radius; i++) {
                int p = in[y * width + Math.max(0, Math.min(width - 1, i))];
                a += getA(p);
                r += getR(p);
                g += getG(p);
                b += getB(p);
            }

            // Fenster über die Zeile/Spalte schieben
            for (int x = 0; x < width; x++) {
                out[y * width + x] = ((int) (a * scale) << 24) | ((int) (r * scale) << 16) | ((int) (g * scale) << 8) | (int) (b * scale);

                int p1 = in[y * width + Math.min(width - 1, x + radius + 1)];
                int p2 = in[y * width + Math.max(0, x - radius)];

                a += getA(p1) - getA(p2);
                r += getR(p1) - getR(p2);
                g += getG(p1) - getG(p2);
                b += getB(p1) - getB(p2);
            }
        }
    }
    private static void blurPassVertical(int[] in, int[] out, int width, int height, int radius) {
        float scale = 1.0f / (radius * 2 + 1);

        for (int y = 0; y < height; y++) {

            float r = 0, g = 0, b = 0, a = 0;

            // Initiales Fenster füllen
            for (int i = -radius; i <= radius; i++) {
                int p = in[y + Math.max(0, Math.min(width - 1, i)) * height];
                a += getA(p);
                r += getR(p);
                g += getG(p);
                b += getB(p);
            }

            // Fenster über die Zeile/Spalte schieben
            for (int x = 0; x < width; x++) {
                out[y + x * height] = ((int) (a * scale) << 24) | ((int) (r * scale) << 16) | ((int) (g * scale) << 8) | (int) (b * scale);

                int p1 = in[y + Math.min(width - 1, x + radius + 1) * height];
                int p2 = in[y + Math.max(0, x - radius) * height];

                a += getA(p1) - getA(p2);
                r += getR(p1) - getR(p2);
                g += getG(p1) - getG(p2);
                b += getB(p1) - getB(p2);
            }
        }
    }

    private static int getB(int p) {
        return p & 0xff;
    }

    private static int getG(int p) {
        return (p >> 8) & 0xff;
    }

    private static int getR(int p) {
        return (p >> 16) & 0xff;
    }

    private static int getA(int p) {
        return (p >> 24) & 0xff;
    }
}
