package dev.screwbox.core.graphics.postfilter;

import java.awt.*;

//TODO save other filters from experiments
public class BeeEyePostFilter implements PostProcessingFilter {
    @Override
    public void apply(Image source, Graphics2D target, PostProcessingContext context) {
        // Wir nutzen die Bounds des aktuellen Viewports/Splitscreens
        var bounds = context.bounds();
        int xMin = bounds.x();
        int yMin = bounds.y();
        int width = bounds.width();
        int height = bounds.height();

        // 1. Hintergrund zeichnen (nur im Bereich des aktuellen Viewports)
        target.drawImage(source, xMin, yMin, xMin + width, yMin + height,
            xMin, yMin, xMin + width, yMin + height, null);

        // Abdunkeln des Bereichs
        target.setColor(new Color(0, 0, 0, 120));
        target.fillRect(xMin, yMin, width, height);

        int eyeSize = 50;
        double centerX = xMin + width / 2.0;
        double centerY = yMin + height / 2.0;
        double maxDist = Math.sqrt(Math.pow(width / 2.0, 2) + Math.pow(height / 2.0, 2));

        // Wir loopen durch die x/y Koordinaten innerhalb der Bounds
        for (int y = yMin; y < yMin + height; y += eyeSize) {
            int xOffset = ((y - yMin) / eyeSize % 2 == 0) ? 0 : eyeSize / 2;

            for (int x = xMin - xOffset; x < xMin + width; x += eyeSize) {

                // Distanz zum Zentrum des aktuellen Viewports berechnen
                double dx = (x + eyeSize / 2.0) - centerX;
                double dy = (y + eyeSize / 2.0) - centerY;
                double dist = Math.sqrt(dx * dx + dy * dy) / maxDist;

                // Dynamischer Zoom basierend auf der Distanz
                double localZoom = 0.9 - (dist * 0.6);

                int srcW = (int) (eyeSize / localZoom);
                int srcH = (int) (eyeSize / localZoom);

                // Quell-Koordinaten im Originalbild berechnen
                int srcX = x - (srcW - eyeSize) / 2;
                int srcY = y - (srcH - eyeSize) / 2;

                target.drawImage(source,
                    x, y, x + eyeSize, y + eyeSize,
                    srcX, srcY, srcX + srcW, srcY + srcH,
                    null);
            }
        }
    }
}
