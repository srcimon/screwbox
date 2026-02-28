package dev.screwbox.core.graphics.filter;

import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.internal.AwtMapper;
import dev.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.VolatileImage;

public class FishEyePostFilter implements PostProcessingFilter {

    private final int gridSize;
    private final double strength;

    public FishEyePostFilter(int gridSize, double strength) {
        Validate.range(gridSize, 2, 128, "grid size must be in range 2 to 128");
        Validate.range(strength, -1, 1, "strength must be in range -1 to 1");
        this.gridSize = gridSize;
        this.strength = strength;
    }

    @Override
    public void apply(final VolatileImage source, final Graphics2D target, final ScreenBounds area, final PostProcessingContext context) {
        target.setColor(AwtMapper.toAwtColor(context.backgroundColor()));
        // Füllt nur den Bereich der zugewiesenen Area
        target.fillRect(area.x(), area.y(), area.width(), area.height());

        int w = area.width();
        int h = area.height();
        double centerX = w / 2.0;
        double centerY = h / 2.0;
        double maxDist = Math.sqrt(centerX * centerX + centerY * centerY);

        for (int y = 0; y < h; y += gridSize) {
            for (int x = 0; x < w; x += gridSize) {
                // Aktuelle Kachel-Grenzen relativ zur Area
                int x2 = Math.min(x + gridSize, w);
                int y2 = Math.min(y + gridSize, h);

                // 1. Ziel-Koordinaten für ALLE VIER ECKEN berechnen (relativ zur Area)
                Point2D pTL = transform(x, y, centerX, centerY, maxDist);
                Point2D pTR = transform(x2, y, centerX, centerY, maxDist);
                Point2D pBL = transform(x, y2, centerX, centerY, maxDist);
                Point2D pBR = transform(x2, y2, centerX, centerY, maxDist);

                // 2. Bounding Box der verzerrten Kachel bestimmen
                int tx = (int) Math.floor(Math.min(pTL.getX(), pBL.getX()));
                int ty = (int) Math.floor(Math.min(pTL.getY(), pTR.getY()));
                int tw = (int) Math.ceil(Math.max(pTR.getX(), pBR.getX())) - tx;
                int th = (int) Math.ceil(Math.max(pBL.getY(), pBR.getY())) - ty;

                // 3. Zeichnen unter Berücksichtigung des Area-Offsets
                // Source-Koordinaten müssen ebenfalls den Area-Offset im Source-Bild beachten
                target.drawImage(source,
                    area.x() + tx, area.y() + ty, area.x() + tx + tw, area.y() + ty + th,
                    area.x() + x, area.y() + y, area.x() + x2, area.y() + y2,
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
