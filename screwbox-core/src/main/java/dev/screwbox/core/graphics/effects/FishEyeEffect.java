package dev.screwbox.core.graphics.effects;

import dev.screwbox.core.graphics.internal.AwtMapper;
import dev.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.VolatileImage;

public class FishEyeEffect implements PostProcessingEffect {

    private final int gridSize;
    private final double strength;

    public FishEyeEffect(int gridSize, double strength) {
        Validate.range(gridSize, 2, 128, "grid size must be in range 1 to 128");
        Validate.range(strength, -1, 1, "strenght must be in range -1 to 1");
        this.gridSize = gridSize;
        this.strength = strength;
    }

    @Override
    public void apply(final VolatileImage source, final Graphics2D target, final PostProcessingContext context) {
        target.setColor(AwtMapper.toAwtColor(context.backgroundColor()));
        target.fillRect(0, 0, source.getWidth(), source.getHeight());

        int w = source.getWidth();
        int h = source.getHeight();
        double centerX = w / 2.0;
        double centerY = h / 2.0;
        double maxDist = Math.sqrt(centerX * centerX + centerY * centerY);

        for (int y = 0; y < h; y += gridSize) {
            for (int x = 0; x < w; x += gridSize) {
                // Aktuelle Kachel-Grenzen im Quellbild
                int x1 = x;
                int y1 = y;
                int x2 = Math.min(x + gridSize, w);
                int y2 = Math.min(y + gridSize, h);

                // 1. Ziel-Koordinaten für ALLE VIER ECKEN berechnen
                // Nur so können wir die Verzerrung der Kanten wirklich abfangen
                Point2D pTL = transform(x1, y1, centerX, centerY, maxDist); // Top Left
                Point2D pTR = transform(x2, y1, centerX, centerY, maxDist); // Top Right
                Point2D pBL = transform(x1, y2, centerX, centerY, maxDist); // Bottom Left
                Point2D pBR = transform(x2, y2, centerX, centerY, maxDist); // Bottom Right

                // 2. Bounding Box der verzerrten Kachel bestimmen
                int tx = (int) Math.floor(Math.min(pTL.getX(), pBL.getX()));
                int ty = (int) Math.floor(Math.min(pTL.getY(), pTR.getY()));
                int tw = (int) Math.ceil(Math.max(pTR.getX(), pBR.getX())) - tx;
                int th = (int) Math.ceil(Math.max(pBL.getY(), pBR.getY())) - ty;

                // 3. Sicherheits-Überlappung (Bleeding)
                // Ein zusätzliches Pixel schließt die diagonalen "Risse"

                target.drawImage(source,
                    tx, ty, tx + tw, ty + th,
                    x1, y1, x2, y2,
                    null);
            }
        }
    }

    private Point2D transform(double x, double y, double cx, double cy, double maxDist) {
        double dx = x - cx;
        double dy = y - cy;
        double dist = Math.sqrt(dx * dx + dy * dy) / maxDist;
        double factor = 1.0 + strength * (dist * dist);
        return new Point2D.Double(cx + dx * factor, cy + dy * factor);
    }
}
