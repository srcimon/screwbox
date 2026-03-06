package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.utils.PerlinNoise;

import java.awt.*;
import java.util.Random;

//TODO support split screen
public class DeepSeeOdyseePostFilter implements PostProcessingFilter {

    private static final float[] FADE = {0f, 0.6f, 1f};
    private static final Color[] COLORES = {new Color(0, 200, 255, 30), new Color(0, 50, 100, 60), new Color(0, 5, 20, 200)};

    private static final int BUBBLE_COUNT = 15;

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        final var area = context.bounds();
        final int x = area.x();
        final int y = area.y();
        final int w = area.width();
        final int h = area.height();

        final double time = context.lifetime().milliseconds() / 1000.0;

        // 1. Hintergrund zeichnen
        target.drawImage(source, x, y, area.maxX(), area.maxY(), x, y, area.maxX(), area.maxY(), null);

        // 2. Lighting / Fog
        target.setPaint(new RadialGradientPaint(x + w / 2f, y + h / 2f, (float) (w * 0.8), FADE, COLORES));
        target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        target.fillRect(x, y, w, h);

        // 3. Blasen (Deterministisch berechnet)
        target.setComposite(AlphaComposite.SrcOver);
        for (int i = 0; i < BUBBLE_COUNT; i++) {
            // Einzigartiger Seed pro Blase
            long bubbleSeed = i * 12345L;

            // Vertikale Bewegung: Startposition + Aufstieg über Zeit (Modulo h für Loop)
            // Wir nutzen Perlin für die Geschwindigkeit, damit nicht alle gleich schnell sind
            double speedMult = 50 + PerlinNoise.generatePerlinNoise(132l, 0.1234, bubbleSeed * 30);
            double initialY = (bubbleSeed % h);
            float relY = (float) ((initialY - time * speedMult) % h);
            if (relY < 0) relY += h;

            // Horizontale Bewegung: Feste Basis-X + Perlin-Wobble
            double initialX = ((bubbleSeed * 0.7) % w);
            double drift = PerlinNoise.generatePerlinNoise(1543l,bubbleSeed, time * 0.5) * 40;
            float relX = (float) ((initialX + drift + w) % w);

            // Größe deterministisch bestimmen
            float size = (float) (2 + (Math.abs(PerlinNoise.generatePerlinNoise(131232l,0.234,bubbleSeed + 99)) * 5));

            drawBubble(target, x + relX, y + relY, size);
        }
    }

    private void drawBubble(Graphics2D target, float drawX, float drawY, float size) {
        int alpha = (int) (50 + (size * 20));
        target.setColor(new Color(255, 255, 255, Math.min(255, alpha)));

        int bw = (int) size;
        int bh = (int) (size * 1.1);

        target.fillOval((int) drawX, (int) drawY, bw, bh);
        target.setColor(new Color(255, 255, 255, 200));
        target.fillOval((int) drawX + 1, (int) drawY + 1, bw / 3, bh / 3);
    }

}
