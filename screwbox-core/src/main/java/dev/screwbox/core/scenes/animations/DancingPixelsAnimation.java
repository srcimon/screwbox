package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.utils.PerlinNoise;

import java.awt.*;
import java.awt.geom.AffineTransform;

//TODO test

/**
 * Splits the screen into pixels that will move somewhat randomly.
 *
 * @param gridSize    size of the pixels from 40 to 240
 * @param isOutsideIn animate from outside to inside
 * @since 3.26.0
 */
//TODO apply resoultionscale
public record DancingPixelsAnimation(int gridSize, boolean isOutsideIn) implements TransitionAnimation {

    public DancingPixelsAnimation() {
        this(40, true);
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final AnimationContext context) {
        final var canvasTransform = target.getTransform();

        for (int y = 0; y < context.height(); y += gridSize) {
            for (int x = 0; x < context.width(); x += gridSize) {
                final double noise = PerlinNoise.generatePerlinNoise3d(1232343L, x * 0.04, y * 0.04, context.progress().value());
                final double distanceToCenter = Math.sqrt(Math.pow(x - context.width() / 2.0, 2) + Math.pow(y - context.height() / 2.0, 2)) / context.width();
                final double effectiveDist = isOutsideIn ? (0.5 - distanceToCenter) : distanceToCenter;
                final double localProgress = Math.clamp((context.progress().value() - effectiveDist * 0.5 - noise * 0.1) / 0.4, 0, 1);

                if (localProgress <= 0) {
                    target.drawImage(source, x, y, x + gridSize + 1, y + gridSize + 1, x, y, x + gridSize, y + gridSize, null);
                } else if (localProgress < 0.98) {

                    double waveOffset = Math.sin(context.progress().value() * Math.PI * 4 + noise * 10) * 10 * localProgress;
                    double scale = 1.0 - Math.pow(localProgress, 2);

                    var transform = new AffineTransform(canvasTransform);
                    transform.translate(x + gridSize / 2.0 + waveOffset, y + gridSize / 2.0 + waveOffset);
                    transform.scale(scale, scale);
                    transform.rotate(noise * localProgress);
                    transform.translate(-gridSize / 2.0, -gridSize / 2.0);

                    target.setTransform(transform);
                    target.drawImage(source, 0, 0, gridSize, gridSize, x, y, x + gridSize, y + gridSize, null);
                    target.setTransform(canvasTransform);
                }
            }
        }
        target.setTransform(canvasTransform);
    }
}
