package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.utils.Validate;

import java.awt.*;

/**
 * Sucks the screen into a black hole.
 *
 * @since 3.27.0
 */
public record BlackHoleAnimation(Size cellSize) implements TransitionAnimation {

    public BlackHoleAnimation() {
        this(Size.square(40));
    }

    public BlackHoleAnimation {
        Validate.isTrue(cellSize::isValid, "cell size must be valid");
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final AnimationContext context) {
        final int tileWidth = (int) (context.resolutionScale() * cellSize.width());
        final int tileHeight = (int) (context.resolutionScale() * cellSize.height());

        final double centerX = context.center().x();
        final double centerY = context.center().y();

        final double maxDistance = Math.sqrt(centerX * centerX + centerY * centerY);
        final double swirlTightness = 5.0;

        for (int y = 0; y < context.height(); y += tileHeight) {
            for (int x = 0; x < context.width(); x += tileWidth) {
                final double distance = Math.sqrt((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY));
                final double relDist = distance / maxDistance;

                final double localProgress = Math.clamp((context.progress().value() - (relDist * 0.5)) / 0.5, 0, 1);

                if (localProgress <= 0) {
                    target.setComposite(AlphaComposite.SrcOver);
                    target.drawImage(source, x, y, x + tileWidth, y + tileHeight, x, y, x + tileWidth, y + tileHeight, null);
                } else {
                    double angle = localProgress * swirlTightness + (1.0 - relDist) * 2.0;

                    double pull = localProgress * localProgress;
                    double scale = 1.0 - localProgress;

                    double currentX = x + (centerX - x) * pull + (Math.sin(angle) * distance * 0.2 * localProgress);
                    double currentY = y + (centerY - y) * pull + (Math.cos(angle) * distance * 0.2 * localProgress);

                    target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (1.0 - localProgress)));

                    target.drawImage(source,
                        (int) currentX, (int) currentY,
                        (int) currentX + (int) (tileWidth * scale), (int) currentY + (int) (tileHeight * scale),
                        x, y,
                        x + tileWidth, y + tileHeight,
                        null);
                }
            }
        }
    }
}