package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Size;
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
        this(240, true);
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final Size size, final Percent progress) {
        final int width = size.width();
        final int height = size.height();

        final AffineTransform canvasTransform = target.getTransform();

        for (int y = 0; y < height; y += gridSize) {
            for (int x = 0; x < width; x += gridSize) {

                double noise = PerlinNoise.generatePerlinNoise3d(1232343L, x * 0.04, y * 0.04, progress.value());

                // Distanz zum Zentrum (0.0 = Mitte, 0.5+ = Rand)
                double distToCenter = Math.sqrt(Math.pow(x - width / 2.0, 2) + Math.pow(y - height / 2.0, 2)) / width;

                // Wenn Outside-In, invertieren wir die Distanz-Gewichtung
                double effectiveDist = isOutsideIn ? (0.5 - distToCenter) : distToCenter;

                // Fortschritt der Welle
                double localP = Math.clamp((progress.value() - effectiveDist * 0.5 - noise * 0.1) / 0.4, 0, 1);

                if (localP <= 0) {
                    target.drawImage(source, x, y, x + gridSize + 1, y + gridSize + 1, x, y, x + gridSize, y + gridSize, null);
                } else if (localP < 0.98) {

                    double waveOffset = Math.sin(progress.value() * Math.PI * 4 + noise * 10) * 10 * localP;
                    double scale = 1.0 - Math.pow(localP, 2);

                    AffineTransform tx = new AffineTransform(canvasTransform);
                    tx.translate(x + gridSize / 2.0 + waveOffset, y + gridSize / 2.0 + waveOffset);
                    tx.scale(scale, scale);
                    tx.rotate(noise * localP);
                    tx.translate(-gridSize / 2.0, -gridSize / 2.0);

                    target.setTransform(tx);
                    target.drawImage(source, 0, 0, gridSize, gridSize, x, y, x + gridSize, y + gridSize, null);
                    target.setTransform(canvasTransform);
                }
            }
        }
        target.setTransform(canvasTransform);
    }
}
