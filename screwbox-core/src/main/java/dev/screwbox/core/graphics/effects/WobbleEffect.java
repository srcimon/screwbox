package dev.screwbox.core.graphics.effects;

import java.awt.*;
import java.awt.image.VolatileImage;

public class WobbleEffect implements PostProcessingEffect {

    @Override
    public void apply(final VolatileImage source, final Graphics2D target, final PostProcessingContext context) {
        int w = source.getWidth();
        int h = source.getHeight();
        int centerX = w / 2;
        int centerY = h / 2;

        double time = context.runtime().milliseconds() / 500.0;
        int iterations = 30; // Anzahl der Segmente von außen nach innen

        // Wir arbeiten uns vom Rand (groß) zur Mitte (klein) vor
        for (int i = 0; i < iterations; i++) {
            // Der Sinus-Shift ist abhängig von der Distanz zum Zentrum
            double wave = Math.sin(time + (i * 0.3));
            int offset = (int) (wave * 12); // Stärke des Auschlags

            // Berechne die Größe des aktuellen "Rahmens"
            int stepW = centerX / iterations;
            int stepH = centerY / iterations;

            // Quell-Koordinaten (Original-Rechteck dieses Schritts)
            int sx1 = i * stepW;
            int sy1 = i * stepH;
            int sx2 = w - sx1;
            int sy2 = h - sy1;

            // Ziel-Koordinaten (mit dem Sinus-Offset versetzt)
            // Das Bild wird hier leicht "aufgepumpt" oder "eingesaugt"
            int dx1 = sx1 - offset;
            int dy1 = sy1 - offset;
            int dx2 = sx2 + offset;
            int dy2 = sy2 + offset;

            // Zeichne nur diesen spezifischen Rahmen
            target.drawImage(source,
                dx1, dy1, dx2, dy2,
                sx1, sy1, sx2, sy2,
                null);
        }
    }
}
