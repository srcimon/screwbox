package dev.screwbox.core.graphics.postfilter;

import java.awt.*;
import java.awt.image.VolatileImage;

public class HeatHazePostFilter implements PostProcessingFilter {
    @Override
    public void apply(Image source, Graphics2D target, PostProcessingContext context) {
        drawLocalHeatHaze(target, source, java.util.List.of(new Rectangle(context.bounds().x(),context.bounds().y(), context.width() / 2, context.height())));
    }

    public void drawLocalHeatHaze(Graphics g, Image screenBuffer, java.util.List<Rectangle> heatZones) {
        Graphics2D g2d = (Graphics2D) g;
        double time = System.currentTimeMillis() / 500.0;

        // 1. Zuerst das komplette Originalbild zeichnen
        g2d.drawImage(screenBuffer, 0, 0, null);

        for (Rectangle zone : heatZones) {
            Shape oldClip = g2d.getClip();
            g2d.setClip(zone);

            int segmentH = 2;
            // Wir iterieren über y, müssen aber die Verschiebung pro x-Position berechnen
            // Da drawImage hier ganze Zeilen-Segmente kopiert, nutzen wir einen
            // durchschnittlichen horizontalen Dämpfungsfaktor oder verfeinern die Segmente.

            for (int y = zone.y; y < zone.y + zone.height; y += segmentH) {

                // Vertikale Intensität (unten stärker)
                double verticalFactor = (double)(y - zone.y) / zone.height;

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
                g2d.drawImage(screenBuffer,
                    zone.x, y, zone.x + zone.width, y + segmentH,
                    zone.x + offsetX, y + offsetY, zone.x + zone.width + offsetX, y + segmentH + offsetY,
                    null);

            }


            g2d.setClip(oldClip);
        }
    }

}
