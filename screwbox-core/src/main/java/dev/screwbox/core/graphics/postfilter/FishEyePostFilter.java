package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.graphics.internal.AwtMapper;
import dev.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.VolatileImage;

public class FishEyePostFilter implements PostProcessingFilter {

    private final int gridSize;
    private final double strength;

    public FishEyePostFilter(final int gridSize, final double strength) {
        Validate.range(gridSize, 2, 128, "grid size must be in range 2 to 128");
        Validate.range(strength, -1, 1, "strength must be in range -1 to 1");
        this.gridSize = gridSize;
        this.strength = strength;
    }

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        final var area = context.bounds();
        target.setColor(AwtMapper.toAwtColor(context.backgroundColor()));
        target.fillRect(area.x(), area.y(), area.width(), area.height());

        final double centerX = area.width() / 2.0;
        final double centerY = area.height() / 2.0;
        final double maxDist = Math.sqrt(centerX * centerX + centerY * centerY);

        for (int y = 0; y < area.height(); y += gridSize) {
            for (int x = 0; x < area.width(); x += gridSize) {
                int x2 = Math.min(x + gridSize, area.width());
                int y2 = Math.min(y + gridSize, area.height());

                Point2D pTL = transform(x, y, centerX, centerY, maxDist);
                Point2D pTR = transform(x2, y, centerX, centerY, maxDist);
                Point2D pBL = transform(x, y2, centerX, centerY, maxDist);
                Point2D pBR = transform(x2, y2, centerX, centerY, maxDist);

                int tx = (int) Math.floor(Math.min(pTL.getX(), pBL.getX()));
                int ty = (int) Math.floor(Math.min(pTL.getY(), pTR.getY()));
                int tw = (int) Math.ceil(Math.max(pTR.getX(), pBR.getX())) - tx;
                int th = (int) Math.ceil(Math.max(pBL.getY(), pBR.getY())) - ty;

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
