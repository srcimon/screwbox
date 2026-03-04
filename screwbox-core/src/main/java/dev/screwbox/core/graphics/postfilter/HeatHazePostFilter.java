package dev.screwbox.core.graphics.postfilter;

import java.awt.*;
import java.util.List;

public class HeatHazePostFilter implements PostProcessingFilter {

    @Override
    public void apply(Image source, Graphics2D target, PostProcessingContext context) {
        Rectangle heatZone = new Rectangle(context.bounds().x(), context.bounds().y(), context.width() / 2, context.height());
        double time = System.currentTimeMillis() / 500.0;
//TODO support viewports
        // 1. Zuerst das komplette Originalbild zeichnen
        target.drawImage(source, 0, 0, null);

        int segmentH = 2;
        // Wir iterieren über y, müssen aber die Verschiebung pro x-Position berechnen
        // Da drawImage hier ganze Zeilen-Segmente kopiert, nutzen wir einen
        // durchschnittlichen horizontalen Dämpfungsfaktor oder verfeinern die Segmente.

        for (int y = heatZone.y; y < heatZone.y + heatZone.height; y += segmentH) {

            // Vertikale Intensität (unten stärker)
            double verticalFactor = (double) (y - heatZone.y) / heatZone.height;

            // Berechnung der Wellenbewegung
            double rawWave = (Math.sin(time * 1.5 + y * 0.1) * 4 +
                              Math.sin(time * 3.7 + y * 0.5) * 2) * verticalFactor;

            // --- NEU: Horizontales Fade-out ---
            // Da wir hier zeilenweise zeichnen, ist ein perfektes pixelgenaues Fade-out
            // pro Pixel in Java2D ohne Shader schwer. Wir können aber die Amplitude
            // der gesamten Zeile dämpfen oder kleinere Segmente nutzen.

            int offsetX = (int) rawWave;
            int offsetY = (int) (Math.abs(Math.sin(time * 0.5 + y * 0.05)) * 3 * verticalFactor);

            // Zeichnen des Segments
            target.drawImage(source,
                heatZone.x, y, heatZone.x + heatZone.width, y + segmentH,
                heatZone.x + offsetX, y + offsetY, heatZone.x + heatZone.width + offsetX, y + segmentH + offsetY,
                null);

        }
    }

}
