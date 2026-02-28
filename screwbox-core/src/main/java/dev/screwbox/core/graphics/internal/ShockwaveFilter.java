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

        // 1. Basisbild zeichnen
        target.drawImage(source, 0, 0, null);

        // 2. Kacheln durchlaufen
        for (int y = 0; y < h; y += tileSize) {
            for (int x = 0; x < w; x += tileSize) {

                double totalOx = 0;
                double totalOy = 0;
                boolean active = false;

                for (Shockwave s : waves) {
                    double dx = x - s.x;
                    double dy = y - s.y;
                    double distSq = dx * dx + dy * dy;
                    double dist = Math.sqrt(distSq);

                    double diff = Math.abs(dist - s.radius);

                    if (diff < s.waveWidth) {
                        // --- NEU: FADEOUT LOGIK ---
                        // Berechnet einen Faktor zwischen 1.0 (Zentrum) und 0.0 (Max Radius)
                        // Voraussetzung: Shockwave Klasse hat ein Feld 'maxRadius'
                        double lifetimeFactor = Math.max(0, 1.0 - (s.radius / s.options.maxRadius()));

                        double falloff = Math.sin((1.0 - diff / s.waveWidth) * Math.PI);

                        // Die Intensität wird mit dem lifetimeFactor multipliziert
                        double force = falloff * s.intensity * lifetimeFactor;

                        if (dist > 0) {
                            totalOx += (dx / dist) * force;
                            totalOy += (dy / dist) * force;
                            active = true;
                        }
                    }
                }

                if (active) {
                    // Clipping verhindern: Sicherstellen, dass destX/Y innerhalb der Source liegen
                    int destX = x + (int) totalOx;
                    int destY = y + (int) totalOy;

                    target.drawImage(source,
                        x, y, x + tileSize, y + tileSize, // Ziel-Kachel Position
                        destX, destY, destX + tileSize, destY + tileSize, // Woher wir die Pixel klauen
                        null);
                }
            }
        }
    }


}
