package dev.screwbox.core.scenes.transitions;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.postfilter.PostProcessingContext;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class GridShredderTransitionFilter implements TransitionAnimation {

    private static final int COLUMNS = 12;
    private static final int ROWS = 8;

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context, Percent progress) {
        final var bounds = context.bounds();
        final double p = progress.value();

        final double tileWidth = (double) bounds.width() / COLUMNS;
        final double tileHeight = (double) bounds.height() / ROWS;

        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                // Individueller Delay pro Kachel basierend auf Position (Schachbrett/Diagonal)
                final double delay = (double) (x + y) / (COLUMNS + ROWS);
                final double localP = Math.max(0, Math.min(1, (p - delay * 0.4) / 0.6));

                if (localP >= 1.0) continue; // Kachel ist komplett weg

                final double xPos = x * tileWidth;
                final double yPos = y * tileHeight;

                // Bewegung: Jede zweite Spalte/Reihe fliegt in eine andere Richtung
                final double directionX = (y % 2 == 0) ? 1 : -1;
                final double directionY = (x % 2 == 0) ? 1 : -1;

                final double offsetX = Math.pow(localP, 2) * bounds.width() * directionX;
                final double offsetY = Math.pow(localP, 2) * bounds.height() * directionY;

                // Skalierung: Kacheln werden kleiner
                final double scale = 1.0 - localP;

                // Rotation: Leichter Spin beim Rausfliegen
                final double rotation = localP * Math.PI * 0.5 * directionX;

                final AffineTransform tx = new AffineTransform();
                // 3. Verschiebung an Zielposition
                tx.translate(xPos + offsetX + tileWidth / 2, yPos + offsetY + tileHeight / 2);
                // 2. Rotation und Skalierung um das Kachelzentrum
                tx.rotate(rotation);
                tx.scale(scale, scale);
                // 1. Ursprung auf Kachelmitte schieben
                tx.translate(-xPos - tileWidth / 2, -yPos - tileHeight / 2);

                drawTile(source, target, xPos, yPos, tileWidth, tileHeight, tx, (float) (1.0 - localP));
            }
        }
    }

    private void drawTile(Image src, Graphics2D g, double x, double y, double w, double h, AffineTransform tx, float alpha) {
        final Shape oldClip = g.getClip();
        final Composite oldComposite = g.getComposite();

        // Clip auf die ursprüngliche Kachelposition
        g.setClip(new Rectangle2D.Double(x, y, w + 1, h + 1)); // +1 gegen Nahtstellen
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, alpha)));

        g.drawImage(src, tx, null);

        g.setComposite(oldComposite);
        g.setClip(oldClip);
    }
}

