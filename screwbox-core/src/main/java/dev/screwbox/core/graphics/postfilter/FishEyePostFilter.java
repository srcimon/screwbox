package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.graphics.internal.AwtMapper;
import dev.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.geom.Point2D;

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
                final int gridX = Math.min(x + gridSize, area.width());
                final int gridY = Math.min(y + gridSize, area.height());

                final Point2D topLeft = transform(x, y, centerX, centerY, maxDist);
                final Point2D topRight = transform(gridX, y, centerX, centerY, maxDist);
                final Point2D bottomLeft = transform(x, gridY, centerX, centerY, maxDist);
                final Point2D bottomRight = transform(gridX, gridY, centerX, centerY, maxDist);

                final int flooredX = (int) Math.floor(Math.min(topLeft.getX(), bottomLeft.getX()));
                final int flooredY = (int) Math.floor(Math.min(topLeft.getY(), topRight.getY()));
                final int ceiledWidth = (int) Math.ceil(Math.max(topRight.getX(), bottomRight.getX())) - flooredX;
                final int ceiledHeight = (int) Math.ceil(Math.max(bottomLeft.getY(), bottomRight.getY())) - flooredY;

                target.drawImage(source,
                    area.x() + flooredX, area.y() + flooredY, area.x() + flooredX + ceiledWidth, area.y() + flooredY + ceiledHeight,
                    area.x() + x, area.y() + y, area.x() + gridX, area.y() + gridY,
                    null);
            }
        }
    }

    private Point2D transform(final double x, final double y, final double centerX, final double centerY, final double maxDist) {
        final double distX = x - centerX;
        final double distY = y - centerY;
        final double dist = Math.sqrt(distX * distX + distY * distY) / maxDist;
        final double factor = 1.0 + strength * (dist * dist);
        return new Point2D.Double(centerX + distX * factor, centerY + distY * factor);
    }
}
