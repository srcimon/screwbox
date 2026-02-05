package dev.screwbox.core.graphics.internal.filter;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

//TODO stack size increase the other way around
public class SizeIncreasingBlurImageFilter extends SizeIncreasingImageFilter {

    public SizeIncreasingBlurImageFilter(final int radius) {
        super(radius);
    }

    public class FastBlur {

        public static void apply(BufferedImage image, int radius) {
            final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
            final int[] temp = new int[pixels.length];
            blurPassHorizontal(pixels, temp, image.getWidth(), image.getHeight(), radius);
            blurPassVertical(temp, pixels, image.getHeight(), image.getWidth(), radius);
        }

        //TODO no more square scaling allows higher blur radius
        //TODO allow higher blur radius values
        //TODO document 28% speed increase on normal scale

        private static void blurPassHorizontal(int[] in, int[] out, int w, int h, int radius) {
            float scale = 1.0f / (radius * 2 + 1);

            for (int y = 0; y < h; y++) {
                int outIdx = y * w;
                int inIdx = y * w;

                float r = 0, g = 0, b = 0, a = 0;

                // Initiales Fenster füllen
                for (int i = -radius; i <= radius; i++) {
                    int p = in[inIdx + Math.max(0, Math.min(w - 1, i))];
                    a += (p >> 24) & 0xff;
                    r += (p >> 16) & 0xff;
                    g += (p >> 8) & 0xff;
                    b += p & 0xff;
                }

                // Fenster über die Zeile/Spalte schieben
                for (int x = 0; x < w; x++) {
                    out[outIdx] = ((int) (a * scale) << 24) | ((int) (r * scale) << 16) | ((int) (g * scale) << 8) | (int) (b * scale);

                    int p1 = in[inIdx + Math.min(w - 1, x + radius + 1)];
                    int p2 = in[inIdx + Math.max(0, x - radius)];

                    a += ((p1 >> 24) & 0xff) - ((p2 >> 24) & 0xff);
                    r += ((p1 >> 16) & 0xff) - ((p2 >> 16) & 0xff);
                    g += ((p1 >> 8) & 0xff) - ((p2 >> 8) & 0xff);
                    b += (p1 & 0xff) - (p2 & 0xff);

                    outIdx++;
                }
            }
        }
        private static void blurPassVertical(int[] in, int[] out, int w, int h, int radius) {
            float scale = 1.0f / (radius * 2 + 1);

            for (int y = 0; y < h; y++) {
                int outIdx =  y;

                float r = 0, g = 0, b = 0, a = 0;

                // Initiales Fenster füllen
                for (int i = -radius; i <= radius; i++) {
                    int p = in[y + Math.max(0, Math.min(w - 1, i)) * h];
                    a += (p >> 24) & 0xff;
                    r += (p >> 16) & 0xff;
                    g += (p >> 8) & 0xff;
                    b += p & 0xff;
                }

                // Fenster über die Zeile/Spalte schieben
                for (int x = 0; x < w; x++) {
                    out[outIdx] = ((int) (a * scale) << 24) | ((int) (r * scale) << 16) | ((int) (g * scale) << 8) | (int) (b * scale);

                    int p1 = in[y + Math.min(w - 1, x + radius + 1) * h];
                    int p2 = in[y + Math.max(0, x - radius) * h];

                    a += ((p1 >> 24) & 0xff) - ((p2 >> 24) & 0xff);
                    r += ((p1 >> 16) & 0xff) - ((p2 >> 16) & 0xff);
                    g += ((p1 >> 8) & 0xff) - ((p2 >> 8) & 0xff);
                    b += (p1 & 0xff) - (p2 & 0xff);

                    outIdx += h;
                }
            }
        }
    }

    @Override
    public BufferedImage apply(final BufferedImage image) {
        final BufferedImage enlarged = super.apply(image);
        FastBlur.apply(enlarged, radius);
        return enlarged;
    }
}
