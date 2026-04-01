package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

//TODO refactor
//TODO test

/**
 * An modern looking animation shredding the scren into pieces.
 *
 * @since 3.26.0
 */
public record GridShredderAnimation(Size gridSize) implements TransitionAnimation {

    public GridShredderAnimation() {
        this(Size.of(12, 10));
    }

    public GridShredderAnimation {
        Validate.isTrue(gridSize::isValid, "grid size must be valid");
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final Size size, final Percent progress) {
        final double p = progress.value();

        final double tileWidth = (double) size.width() / gridSize.width();
        final double tileHeight = (double) size.height() / gridSize.height();

        for (int y = 0; y < gridSize.height(); y++) {
            for (int x = 0; x < gridSize.width(); x++) {
                // Individueller Delay pro Kachel basierend auf Position (Schachbrett/Diagonal)
                final double delay = (double) (x + y) / (gridSize.width() + gridSize.height());
                final double localP = Math.clamp((p - delay * 0.4) / 0.6, 0, 1);

                if (localP >= 1.0) continue; // Kachel ist komplett weg

                final double xPos = x * tileWidth;
                final double yPos = y * tileHeight;

                // Bewegung: Jede zweite Spalte/Reihe fliegt in eine andere Richtung
                final double directionX = (y % 2 == 0) ? 1 : -1;
                final double directionY = (x % 2 == 0) ? 1 : -1;

                final double offsetX = Math.pow(localP, 2) * size.width() * directionX;
                final double offsetY = Math.pow(localP, 2) * size.height() * directionY;

                // Skalierung: Kacheln werden kleiner
                final double scale = 1.0 - localP;

                final AffineTransform tx = new AffineTransform();
                // 3. Verschiebung an Zielposition
                tx.translate(xPos + offsetX + tileWidth / 2, yPos + offsetY + tileHeight / 2);
                // 2. Rotation und Skalierung um das Kachelzentrum
                tx.rotate(localP * Math.PI * 0.5 * directionX);
                tx.scale(scale, scale);
                // 1. Ursprung auf Kachelmitte schieben
                tx.translate(-xPos - tileWidth / 2, -yPos - tileHeight / 2);


                // Clip auf die ursprüngliche Kachelposition
                target.setClip(new Rectangle2D.Double(xPos, yPos, tileWidth + 1, tileHeight + 1)); // +1 gegen Nahtstellen
                target.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, (float) (1.0 - localP))));

                target.drawImage(source, tx, null);

            }
        }
    }

}

