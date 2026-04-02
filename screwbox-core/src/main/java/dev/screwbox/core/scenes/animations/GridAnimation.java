package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.utils.Validate;

import java.awt.*;

/**
 * Splits the screen into a grid and fades out the single cells one by one.
 *
 * @since 3.26.0
 */
//TODO apply resoultionscale
public record GridAnimation(int gridSize) implements TransitionAnimation {

    /**
     * Creates an instance with default grid size.
     */
    public GridAnimation {
        Validate.range(gridSize, 32, 480, "grid size must in range 32 to 480");
    }

    public GridAnimation() {
        this(32);
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final AnimationContext context) {
        for (int y = 0; y < context.height(); y += gridSize) {
            for (int x = 0; x < context.width(); x += gridSize) {
                double blockDelay = ((double) x / context.width() * 0.4) + ((double) y / context.height() * 0.3);
                double localProgress = Math.clamp((context.progress().value() - blockDelay) / 0.3, 0, 1);

                if (localProgress < 1.0) {
                    final int offsetX = (y % (gridSize * 2) == 0) ? (int) (localProgress * 50) : (int) (-localProgress * 50);
                    final int offsetY = (x % (gridSize * 2) == 0) ? (int) (localProgress * 30) : (int) (-localProgress * 30);
                    final int drawW = (int) (gridSize * (1.0 - localProgress));
                    final int drawH = (int) (gridSize * (1.0 - localProgress * 0.5)); // Behält vertikale Linie länger

                    target.setClip(x, y, gridSize, gridSize);
                    target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (1.0 - localProgress)));
                    target.drawImage(source,
                        x + offsetX, y + offsetY,
                        x + offsetX + drawW, y + offsetY + drawH,
                        x, y, x + gridSize, y + gridSize,
                        null);
                }
            }
        }
    }
}
