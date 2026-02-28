package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.filter.PostProcessingContext;
import dev.screwbox.core.graphics.filter.PostProcessingFilter;

import java.awt.*;
import java.awt.image.VolatileImage;
import java.util.List;
import java.util.Random;

//TODO add support for split screen mode
class ShockwaveFilter implements PostProcessingFilter {


    private final List<Shockwave> waves;
    private final int tileSize;

    ShockwaveFilter(final List<Shockwave> waves, int tileSize) {
        this.waves = waves;
        this.tileSize = tileSize;
    }

    @Override
    public void apply(VolatileImage source, Graphics2D target, ScreenBounds area, PostProcessingContext context) {
        int w = source.getWidth();
        int h = source.getHeight();
        // 1. Zuerst das Basisbild einmal zeichnen
        target.drawImage(source, 0, 0, null);

        // 2. Den Bildschirm in Kacheln durchlaufen
        for (int y = 0; y < h; y += tileSize) {
            for (int x = 0; x < w; x += tileSize) {

                double totalOx = 0;
                double totalOy = 0;
                boolean active = false;

                // 3. Für jede Kachel den Einfluss ALLER Wellen berechnen
                for (Shockwave s : waves) {
                    double dx = x - s.x;
                    double dy = y - s.y;
                    double distSq = dx * dx + dy * dy; // Quadrat für Performance
                    double dist = Math.sqrt(distSq);

                    double diff = Math.abs(dist - s.radius);
                    if (diff < s.waveWidth) {
                        double falloff = Math.sin((1.0 - diff / s.waveWidth) * Math.PI);
                        double force = falloff * s.intensity;
                        if (dist > 0) {
                            totalOx += (dx / dist) * force;
                            totalOy += (dy / dist) * force;
                            active = true;
                        }
                    }
                }

                // 4. Nur zeichnen, wenn mindestens eine Welle die Kachel beeinflusst
                if (active) {
                    int destX = x + (int) totalOx;
                    int destY = y + (int) totalOy;
                    target.drawImage(source,
                        destX, destY, destX + tileSize, destY + tileSize, // Ziel (verschoben)
                        x, y, x + tileSize, y + tileSize,                 // Quelle (original)
                        null);
                }
            }
        }
    }


}
