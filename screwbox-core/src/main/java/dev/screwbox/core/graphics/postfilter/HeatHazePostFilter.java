package dev.screwbox.core.graphics.postfilter;

import java.awt.*;

public class HeatHazePostFilter implements PostProcessingFilter {

    @Override
    public void apply(Image source, Graphics2D target, PostProcessingContext context) {
        Rectangle heatZone = new Rectangle(context.bounds().x(), context.bounds().y(), context.width() / 2, context.height());
        double time = System.currentTimeMillis() / 500.0;
//TODO support viewports
        // 1. Zuerst das komplette Originalbild zeichnen
        target.drawImage(source, 0, 0, null);

        int segmentH = 2;//TODO configure
        for (int y = heatZone.y; y < heatZone.y + heatZone.height; y += segmentH) {

            final double verticalFactor = (double) (y - heatZone.y) / heatZone.height;

            final int offsetX = (int) ((Math.sin(time * 1.5 + y * 0.1) * 4 +
                                     Math.sin(time * 3.7 + y * 0.5) * 2) * verticalFactor);

            final int offsetY = (int) (Math.abs(Math.sin(time * 0.5 + y * 0.05)) * 3 * verticalFactor);

            target.drawImage(source,
                heatZone.x, y, heatZone.x + heatZone.width, y + segmentH,
                heatZone.x + offsetX, y + offsetY, heatZone.x + heatZone.width + offsetX, y + segmentH + offsetY,
                null);

        }
    }

}
