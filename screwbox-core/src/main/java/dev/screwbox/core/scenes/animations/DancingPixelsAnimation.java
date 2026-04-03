package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.utils.PerlinNoise;
import dev.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Splits the screen into pixels that will move somewhat randomly.
 *
 * @param cellSize    size of a single cell in pixels
 * @param isOutsideIn animate from outside to inside
 * @since 3.26.0
 */
public record DancingPixelsAnimation(Size cellSize, boolean isOutsideIn) implements TransitionAnimation {

    public DancingPixelsAnimation {
        Validate.isTrue(cellSize::isValid, "cell size must be valid");
    }

    public DancingPixelsAnimation() {
        this(Size.of(32, 32), true);
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final AnimationContext context) {
        final int tileWidth = (int) (context.resolutionScale() * cellSize.width());
        final int tileHeight = (int) (context.resolutionScale() * cellSize.height());
        final var canvasTransform = target.getTransform();

        for (int y = 0; y < context.height(); y += tileHeight) {
            for (int x = 0; x < context.width(); x += tileWidth) {
                final double noise = PerlinNoise.generatePerlinNoise3d(1232343L, x * 0.04, y * 0.04, context.progress().value());
                final double distanceToCenter = Math.sqrt(Math.pow(x - context.width() / 2.0, 2) + Math.pow(y - context.height() / 2.0, 2)) / context.width();
                final double effectiveDist = isOutsideIn ? (0.5 - distanceToCenter) : distanceToCenter;
                final double localProgress = Math.clamp((context.progress().value() - effectiveDist * 0.5 - noise * 0.1) / 0.4, 0, 1);

                if (localProgress <= 0) {
                    target.drawImage(source, x, y, x + tileWidth + 1, y + tileHeight + 1, x, y, x + tileWidth, y + tileHeight, null);
                } else if (localProgress < 0.98) {

                    double waveOffset = Math.sin(context.progress().value() * Math.PI * 4 + noise * 10) * 10 * localProgress;
                    double scale = 1.0 - Math.pow(localProgress, 2);

                    var transform = new AffineTransform(canvasTransform);
                    transform.translate(x + tileWidth / 2.0 + waveOffset, y + tileHeight / 2.0 + waveOffset);
                    transform.scale(scale, scale);
                    transform.rotate(noise * localProgress);
                    transform.translate(-tileWidth / 2.0, -tileHeight / 2.0);

                    target.setTransform(transform);
                    target.drawImage(source, 0, 0, tileWidth, tileHeight, x, y, x + tileWidth, y + tileHeight, null);
                    target.setTransform(canvasTransform);
                }
            }
        }
        target.setTransform(canvasTransform);
    }
}
