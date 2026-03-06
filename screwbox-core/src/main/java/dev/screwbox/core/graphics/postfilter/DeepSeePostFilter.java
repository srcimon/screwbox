package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.utils.PerlinNoise;
import dev.screwbox.core.utils.Validate;

import java.awt.*;

/**
 * Adds deep see feeling to the scene. Can be combined with {@link UnderwaterPostFilter}.
 *
 * @since 3.24.0
 */
public record DeepSeePostFilter(int bubbleCount) implements PostProcessingFilter {

    private static final float[] FADE = {0f, 0.6f, 1f};
    private static final Color[] COLORES = {new Color(0, 200, 255, 30), new Color(0, 50, 100, 60), new Color(0, 5, 20, 200)};

    public DeepSeePostFilter() {
        this(30);
    }
    public DeepSeePostFilter {
        Validate.positive(bubbleCount, "bubble count must be positive");
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        final var area = context.bounds();
        final int x = area.x();
        final int y = area.y();
        final int w = area.width();
        final int h = area.height();

        final double time = context.lifetime().milliseconds() / 1000.0;
        target.drawImage(source, x, y, area.maxX(), area.maxY(), x, y, area.maxX(), area.maxY(), null);
        target.setPaint(new RadialGradientPaint(x + w / 2f, y + h / 2f, (float) (w * 0.8), FADE, COLORES));
        target.fillRect(x, y, w, h);

        for (int i = 0; i < bubbleCount; i++) {
            final long bubbleSeed = i * i * 1575145L;
            final double speedMult = 50 + PerlinNoise.generatePerlinNoise(bubbleSeed, 0.1234, bubbleSeed * 30);
            final double initialY = (bubbleSeed % h);
            float relY = (float) ((initialY - time * speedMult) % h);
            if (relY < 0) relY += h;

            final double initialX = ((bubbleSeed * 0.7) % w);
            final double drift = PerlinNoise.generatePerlinNoise(bubbleSeed,bubbleSeed, time * 0.5) * 40;
            final float relX = (float) ((initialX + drift + w) % w);
            final float size = (float) (2 + (Math.abs(PerlinNoise.generatePerlinNoise(bubbleSeed,0.234,bubbleSeed + 99)) * 5));

            final float xxx = (float) Math.abs(PerlinNoise.generatePerlinNoise(bubbleSeed,bubbleSeed, time * 0.8)*0.2);
            target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, xxx+0.5f));
            drawBubble(target, x + relX, y + relY, size);
        }
    }

    private void drawBubble(final Graphics2D target, float drawX, float drawY, float size) {
        int alpha = (int) (50 + (size * 20));
        target.setColor(new Color(255, 255, 255, Math.min(255, alpha)));

        target.fillOval((int) drawX, (int) drawY, (int) size, (int) (size * 1.1));
        target.setColor(Color.WHITE);
        target.fillOval((int) drawX + 1, (int) drawY + 1, (int) size / 3, (int) (size * 1.1) / 3);
    }

}
