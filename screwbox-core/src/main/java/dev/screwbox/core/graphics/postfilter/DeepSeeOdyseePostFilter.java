package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.utils.PerlinNoise;

import java.awt.*;
import java.util.Random;

//TODO support split screen
public class DeepSeeOdyseePostFilter implements PostProcessingFilter {

    private float[] bubbleX, bubbleY, bubbleSpeed, bubbleSize;

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        final var area = context.bounds();
        int x = area.x();
        int y = area.y();
        int w = area.width();
        int h = area.height();

        long time = context.lifetime().milliseconds();
        double zTime = time / 2000.0;

        target.drawImage(source,
            area.x(), area.y(), area.maxX(), area.maxY(),
            area.x(), area.y(), area.maxX(), area.maxY(),
            null);

        // 2. DEEP SEA LIGHTING (Caustics & Fog)
        float[] d = {0f, 0.6f, 1f};
        Color[] c = {new Color(0, 200, 255, 30), new Color(0, 50, 100, 60), new Color(0, 5, 20, 200)};
        // Gradient relativ zur Viewport-Mitte
        RadialGradientPaint deepBlue = new RadialGradientPaint(x + w / 2f, y + h / 2f, (float) (w * 0.8), d, c);
        target.setPaint(deepBlue);
        target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        target.fillRect(x, y, w, h);

        // Initialisierung der Blasen relativ zur Viewport-Größe
        if (bubbleX == null) {
            int bCount = 15;
            bubbleX = new float[bCount];
            bubbleY = new float[bCount];
            bubbleSpeed = new float[bCount];
            bubbleSize = new float[bCount];
            Random r = new Random();
            for (int i = 0; i < bCount; i++) {
                bubbleX[i] = r.nextFloat(w);
                bubbleY[i] = r.nextFloat(h);
                bubbleSpeed[i] = 1.0f + r.nextFloat() * 2.0f;
                bubbleSize[i] = 2 + r.nextInt(5);
            }
        }

        target.setComposite(AlphaComposite.SrcOver);
        for (int i = 0; i < bubbleX.length; i++) {
            double horizontalDrift = PerlinNoise.generatePerlinNoise3d(220L, (x + bubbleX[i]) * 0.01, (y + bubbleY[i]) * 0.01, zTime);

            bubbleY[i] -= bubbleSpeed[i];
            bubbleX[i] += (float) (horizontalDrift * 2.0 + Math.sin(zTime + i) * 0.5);

            // Wrapping innerhalb der Viewport-Grenzen
            if (bubbleY[i] < -20) {
                bubbleY[i] = h + 20;
                bubbleX[i] = new Random().nextInt(w);
            }
            bubbleX[i] = (bubbleX[i] + w) % w;

            int alpha = (int) (50 + (bubbleSize[i] * 20));
            target.setColor(new Color(255, 255, 255, Math.min(255, alpha)));

            int bw = (int) bubbleSize[i];
            int bh = (int) (bubbleSize[i] * 1.1);

            // Zeichnen mit Offset x und y
            int drawX = (int) (x + bubbleX[i]);
            int drawY = (int) (y + bubbleY[i]);

            target.fillOval(drawX, drawY, bw, bh);
            target.setColor(new Color(255, 255, 255, 200));
            target.fillOval(drawX + 1, drawY + 1, bw / 3, bh / 3);
        }
    }

}
