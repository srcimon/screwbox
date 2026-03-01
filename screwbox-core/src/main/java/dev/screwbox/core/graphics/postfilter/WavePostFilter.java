package dev.screwbox.core.graphics.postfilter;

import dev.screwbox.core.graphics.internal.AwtMapper;

import java.awt.*;
import java.awt.image.VolatileImage;

public class WavePostFilter implements PostProcessingFilter {

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        final var area = context.bounds();
        target.setColor(AwtMapper.toAwtColor(context.backgroundColor()));
        target.fillRect(area.x(), area.y(), area.width(), area.height());
        int w = area.width();
        int h = area.height();

        double time = context.runtime().milliseconds() / 500.0;
        double waveIntensity = 20.0;
        double frequency = 0.05;

        int rowHeight = 4;


        for (int y = 0; y < h; y += rowHeight) {
            // Berechne den Versatz relativ zur aktuellen Zeile innerhalb der Area
            int offsetX = (int) (Math.sin((y * frequency) + time) * waveIntensity);

            // Ziel-Koordinaten: area.x() + offsetX verschiebt die Zeile horizontal
            // Quell-Koordinaten: area.x() liest den korrekten Bereich aus dem Source-Bild
            target.drawImage(source,
                area.x() + offsetX, area.y() + y, area.x() + w + offsetX, area.y() + y + rowHeight,
                area.x(), area.y() + y, area.x() + w, area.y() + y + rowHeight,
                null);
        }
    }
}
