package dev.screwbox.core.graphics.internal.filter;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBufferInt;
import java.awt.image.Kernel;
import java.util.Arrays;

public class SizeIncreasingBlurImageFilter extends SizeIncreasingImageFilter {

    private final ConvolveOp horizontalOp;
    private final ConvolveOp verticalOp;

    public class FastBlur {

        public static void apply(BufferedImage image, int radius) {
            if (radius < 1) return;

            int w = image.getWidth();
            int h = image.getHeight();

            int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
            int[] temp = new int[pixels.length];

            blurPass(pixels, temp, w, h, radius, true);
            blurPass(temp, pixels, h, w, radius, false);
        }

        private static void blurPass(int[] in, int[] out, int w, int h, int radius, boolean horizontal) {
            float scale = 1.0f / (radius * 2 + 1);

            for (int y = 0; y < h; y++) {
                int outIdx = horizontal ? y * w : y;
                int inIdx = horizontal ? y * w : y;

                float r = 0, g = 0, b = 0, a = 0;

                for (int i = -radius; i <= radius; i++) {
                    int p = in[inIdx + Math.max(0, Math.min(w - 1, i)) * (horizontal ? 1 : h)];
                    a += (p >> 24) & 0xff;
                    r += (p >> 16) & 0xff;
                    g += (p >> 8) & 0xff;
                    b += p & 0xff;
                }

                for (int x = 0; x < w; x++) {
                    out[outIdx] = ((int)(a * scale) << 24) | ((int)(r * scale) << 16) | ((int)(g * scale) << 8) | (int)(b * scale);

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

    public SizeIncreasingBlurImageFilter(final int radius) {
        super(radius);
        // Use a 1D array for weights
        final float weight = 1f / radius;
        final float[] data = new float[radius];
        Arrays.fill(data, weight);

        // Create two 1D kernels
        final Kernel hKernel = new Kernel(radius, 1, data);
        final Kernel vKernel = new Kernel(1, radius, data);

        // EDGE_NO_OP is fastest, but consider your padding from the superclass
        this.horizontalOp = new ConvolveOp(hKernel, ConvolveOp.EDGE_NO_OP, null);
        this.verticalOp = new ConvolveOp(vKernel, ConvolveOp.EDGE_NO_OP, null);
    }

    @Override
    public BufferedImage apply(final BufferedImage image) {
        // 1. Get the enlarged canvas from super
        FastBlur.apply(image, radius);
       return image;
    }
}
