package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.utils.PerlinNoise;
import dev.screwbox.core.utils.Validate;

import java.awt.*;

/**
 * Adds deep sea feeling to the scene. Can be combined with {@link UnderwaterPostFilter}.
 *
 * @since 3.24.0
 */
public record DeepSeaPostFilter(int bubbleCount) implements PostProcessingFilter {

    private static final float[] FADE = {0f, 0.6f, 1f};
    private static final Color[] COLORES = {new Color(0, 200, 255, 30), new Color(0, 50, 100, 60), new Color(0, 5, 20, 200)};

    public DeepSeaPostFilter() {
        this(30);
    }

    public DeepSeaPostFilter {
        Validate.positive(bubbleCount, "bubble count must be positive");
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        final var area = context.bounds();
        final int x = area.x();
        final int y = area.y();
        final int w = area.width();
        final int h = area.height();
        final double scale = context.resolutionScale();

        final double time = context.lifetime().milliseconds() / 1000.0;
        drawSourceImage(source, target, context);

        target.setPaint(new RadialGradientPaint(x + w / 2f, y + h / 2f, (float) (w * 0.8), FADE, COLORES));
        target.fillRect(x, y, w, h);

        for (int i = 0; i < bubbleCount; i++) {
            final long bubbleSeed = i * i * 1575145L;
            final double speedMult = (50 + PerlinNoise.generatePerlinNoise(bubbleSeed, 0.1234, bubbleSeed * 30.0)) * scale;
            final double initialY = (bubbleSeed % h);
            float relY = (float) ((initialY - time * speedMult) % h);
            if (relY < 0) relY += h;

            final double initialX = ((bubbleSeed * 0.7) % w);
            final double drift = PerlinNoise.generatePerlinNoise(bubbleSeed, bubbleSeed, time * 0.5) * 40 * scale;
            final float relX = (float) ((initialX + drift + w) % w);

            final float size = (float) ((2 + (Math.abs(PerlinNoise.generatePerlinNoise(bubbleSeed, 0.234, bubbleSeed + 99.0)) * 5)) * scale);
            final float alphaNoise = (float) Math.abs(PerlinNoise.generatePerlinNoise(bubbleSeed, bubbleSeed, time * 0.8) * 0.2);
            target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, alphaNoise + 0.5f)));

            drawBubble(target, x + relX, y + relY, size);
        }
    }

    private void drawBubble(final Graphics2D target, float drawX, float drawY, float size) {
        // Alpha basiert auf der skalierten Größe, daher bleibt der visuelle Effekt konsistent
        int alpha = (int) (50 + (size / 2.0 * 20)); // Justierung, da size nun mitskaliert
        target.setColor(new Color(255, 255, 255, Math.min(255, alpha)));

        target.fillOval((int) drawX, (int) drawY, (int) size, (int) (size * 1.1));
        target.setColor(Color.WHITE);

        // Highlights proportional zur skalierten Größe
        int highlightSize = Math.max(1, (int) (size / 3));
        target.fillOval((int) drawX + 1, (int) drawY + 1, highlightSize, (int) (size * 1.1) / 3);
    }

}
