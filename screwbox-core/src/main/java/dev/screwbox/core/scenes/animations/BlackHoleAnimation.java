package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Saugt den Bildschirm in ein schwarzes Loch im Zentrum ein.
 *
 * @since 3.26.0
 */
public record BlackHoleAnimation(Size cellSize) implements TransitionAnimation {

    public BlackHoleAnimation() {
        this(Size.square(50)); // Kleinere Zellen für flüssigeren Effekt
    }

    public BlackHoleAnimation {
        Validate.isTrue(cellSize::isValid, "cell size must be valid");
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final AnimationContext context) {
        final int tileWidth = (int) (context.resolutionScale() * cellSize.width());
        final int tileHeight = (int) (context.resolutionScale() * cellSize.height());

        final int width = context.width();
        final int height = context.height();
        final double centerX = width / 2.0;
        final double centerY = height / 2.0;
        final double progress = context.progress().value();

        // Vorberechnet, um Math.sqrt in der Schleife zu vermeiden
        final double maxDistanceSq = centerX * centerX + centerY * centerY;
        final double swirlTightness = 5.0;

        for (int y = 0; y < height; y += tileHeight) {
            for (int x = 0; x < width; x += tileWidth) {
                double dx = x - centerX;
                double dy = y - centerY;
                // Nutze Quadrat der Distanz für den Check (schneller als sqrt)
                double distSq = dx * dx + dy * dy;
                double relDist = distSq / maxDistanceSq;

                double localProgress = Math.clamp((progress - (relDist * 0.5)) / 0.5, 0, 1);

                if (localProgress <= 0) {
                    target.setComposite(AlphaComposite.SrcOver);
                    target.drawImage(source, x, y, x + tileWidth, y + tileHeight, x, y, x + tileWidth, y + tileHeight, null);
                    continue;
                }

                if (localProgress < 1.0) {
                    double distance = Math.sqrt(distSq); // Erst hier sqrt, wenn wirklich nötig
                    double angle = localProgress * swirlTightness + (1.0 - relDist) * 2.0;

                    // Pull-Faktor (einfaches x*x statt Math.pow)
                    double pull = localProgress * localProgress;
                    double scale = 1.0 - localProgress;

                    double currentX = x + (centerX - x) * pull + (Math.sin(angle) * distance * 0.2 * localProgress);
                    double currentY = y + (centerY - y) * pull + (Math.cos(angle) * distance * 0.2 * localProgress);

                    int dW = (int) (tileWidth * scale);
                    int dH = (int) (tileHeight * scale);

                    // Nur ändern, wenn nötig (Composite-Wechsel sind teuer)
                    target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (1.0 - localProgress)));

                    target.drawImage(source,
                        (int) currentX, (int) currentY, (int) currentX + dW, (int) currentY + dH,
                        x, y, x + tileWidth, y + tileHeight,
                        null);
                }
            }
        }
    }
}