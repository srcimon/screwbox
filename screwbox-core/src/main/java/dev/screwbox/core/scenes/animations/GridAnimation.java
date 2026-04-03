package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.graphics.Size;

import java.awt.*;

/**
 * Splits the screen into a grid and fades out the single cells one by one.
 *
 * @since 3.26.0
 */
public record GridAnimation(Size gridSize) implements TransitionAnimation {

    public GridAnimation() {
        this(Size.of(30, 20));
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final AnimationContext context) {
        final int tileWidth = context.width() / gridSize.width();
        final int tileHeight = context.height() / gridSize.height();

        for (int y = 0; y < context.height(); y += tileHeight) {
            for (int x = 0; x < context.width(); x += tileWidth) {
                double blockDelay = ((double) x / context.width() * 0.4) + ((double) y / context.height() * 0.3);
                double localProgress = Math.clamp((context.progress().value() - blockDelay) / 0.3, 0, 1);

                if (localProgress < 1.0) {
                    final int offsetX = (y % (tileWidth * 2) == 0) ? (int) (localProgress * 50) : (int) (-localProgress * 50);
                    final int offsetY = (x % (tileHeight * 2) == 0) ? (int) (localProgress * 30) : (int) (-localProgress * 30);

                    final int drawW = (int) (tileWidth * (1.0 - localProgress));
                    final int drawH = (int) (tileHeight * (1.0 - localProgress * 0.5));

                    target.setClip(x, y, tileWidth, tileHeight);
                    target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (1.0 - localProgress)));

                    target.drawImage(source,
                        x + offsetX, y + offsetY,
                        x + offsetX + drawW, y + offsetY + drawH,
                        x, y, x + tileWidth, y + tileHeight,
                        null);
                }
            }
        }
    }
}
