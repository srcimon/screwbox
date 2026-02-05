package dev.screwbox.core.graphics.internal.filter;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class SizeIncreasingBlurImageFilter extends SizeIncreasingImageFilter {

    public SizeIncreasingBlurImageFilter(final int radius) {
        super(radius);
    }

    public class FastBlur {

        public static void apply(BufferedImage image, int radius) {
            final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
            final int[] temp = new int[pixels.length];
            blurPass(pixels, temp, image.getWidth(), image.getHeight(), radius, true);
            blurPass(temp, pixels, image.getHeight(), image.getWidth(), radius, false);
        }

        //TODO no more square scaling allows higher blur radius
        //TODO allow higher blur radius values
        //TODO document 28% speed increase on normal scale
        private static void blurPass(int[] in, int[] out, int w, int h, int radius, boolean horizontal) {
            float scale = 1.0f / (radius * 2 + 1);

            for (int y = 0; y < h; y++) {
                int outIdx = horizontal ? y * w : y;
                int inIdx = horizontal ? y * w : y;

                float r = 0, g = 0, b = 0, a = 0;

                // Initiales Fenster füllen
                for (int i = -radius; i <= radius; i++) {
                    int p = in[inIdx + Math.max(0, Math.min(w - 1, i)) * (horizontal ? 1 : h)];
                    a += (p >> 24) & 0xff;
                    r += (p >> 16) & 0xff;
                    g += (p >> 8) & 0xff;
                    b += p & 0xff;
                }

                // Fenster über die Zeile/Spalte schieben
                for (int x = 0; x < w; x++) {
                    out[outIdx] = ((int) (a * scale) << 24) | ((int) (r * scale) << 16) | ((int) (g * scale) << 8) | (int) (b * scale);

                    int p1 = in[inIdx + Math.min(w - 1, x + radius + 1) * (horizontal ? 1 : h)];
                    int p2 = in[inIdx + Math.max(0, x - radius) * (horizontal ? 1 : h)];

                    a += ((p1 >> 24) & 0xff) - ((p2 >> 24) & 0xff);
                    r += ((p1 >> 16) & 0xff) - ((p2 >> 16) & 0xff);
                    g += ((p1 >> 8) & 0xff) - ((p2 >> 8) & 0xff);
                    b += (p1 & 0xff) - (p2 & 0xff);

                    outIdx += (horizontal ? 1 : h);
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
