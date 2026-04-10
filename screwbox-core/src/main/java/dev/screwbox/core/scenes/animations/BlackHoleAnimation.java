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
        this(Size.square(35)); // Kleinere Zellen für flüssigeren Effekt
    }

    public BlackHoleAnimation {
        Validate.isTrue(cellSize::isValid, "cell size must be valid");
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final AnimationContext context) {
        final int tileWidth = (int) (context.resolutionScale() * cellSize.width());
        final int tileHeight = (int) (context.resolutionScale() * cellSize.height());

        final double centerX = context.width() / 2.0;
        final double centerY = context.height() / 2.0;
        final double progress = context.progress().value();

        // Stärke der Rotation (je höher, desto mehr Wirbel)
        final double swirlTightness = 5.0;

        for (int y = 0; y < context.height(); y += tileHeight) {
            for (int x = 0; x < context.width(); x += tileWidth) {
                double dx = x - centerX;
                double dy = y - centerY;
                double distance = Math.sqrt(dx * dx + dy * dy);
                double maxDistance = Math.sqrt(centerX * centerX + centerY * centerY);

                // Normalisierte Distanz (0 = Mitte, 1 = Ecke)
                double relDist = distance / maxDistance;

                // Der "Event Horizon": Zellen werden erst aktiv, wenn der Progress ihre Distanz erreicht
                double localProgress = Math.clamp((progress - (relDist * 0.5)) / 0.5, 0, 1);

                if (localProgress > 0 && localProgress < 1.0) {
                    // Spirale: Winkel ändert sich basierend auf Progress UND Distanz zur Mitte
                    double angle = localProgress * swirlTightness + (1.0 - relDist) * 2.0;

                    // Die Zellen bewegen sich auf einer Kurve zur Mitte
                    double cos = Math.cos(angle);
                    double sin = Math.sin(angle);

                    // Neue Position berechnen (Interpolation zwischen Start und Zentrum)
                    double pull = Math.pow(localProgress, 2); // Exponentieller Sog
                    double currentX = x + (centerX - x) * pull + (sin * distance * 0.2 * localProgress);
                    double currentY = y + (centerY - y) * pull + (cos * distance * 0.2 * localProgress);

                    double scale = 1.0 - localProgress;

                    AffineTransform transform = new AffineTransform();
                    transform.translate(currentX, currentY);
                    transform.scale(scale, scale);
                    transform.rotate(angle);

                    target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (1.0 - localProgress)));

                    // Zeichne den spezifischen Ausschnitt des Quellbildes an der transformierten Position
                    target.setClip(null); // Reset clip for safety
                    target.drawImage(source,
                        (int) currentX, (int) currentY,
                        (int) (currentX + tileWidth * scale), (int) (currentY + tileHeight * scale),
                        x, y, x + tileWidth, y + tileHeight,
                        null);
                } else if (localProgress <= 0) {
                    // Noch nicht vom Sog erfasst: Normal zeichnen
                    target.setComposite(AlphaComposite.SrcOver);
                    target.drawImage(source, x, y, x + tileWidth, y + tileHeight, x, y, x + tileWidth, y + tileHeight, null);
                }
            }
        }
    }
}