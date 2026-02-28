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
    public void apply(final VolatileImage source, final Graphics2D target, final ScreenBounds area, final PostProcessingContext context) {
        int w = area.size().width();
        int h = area.size().height();
        int offsetX = area.offset().x();
        int offsetY = area.offset().y();

        target.drawImage(source, offsetX, offsetY, area.width(), area.height(), null);

        // 2. Kacheln relativ zur Area durchlaufen
        for (int y = 0; y < h; y += tileSize) {
            for (int x = 0; x < w; x += tileSize) {

                double totalOx = 0;
                double totalOy = 0;
                boolean active = false;

                // Welt-Position der Kachel berechnen (wichtig für den Vergleich mit Shockwave-Koordinaten)
                int worldX = offsetX + x;
                int worldY = offsetY + y;

                for (Shockwave wave : waves) {
                    double dx = worldX - wave.x;
                    double dy = worldY - wave.y;
                    double distSq = dx * dx + dy * dy;
                    double dist = Math.sqrt(distSq);

                    double diff = Math.abs(dist - wave.radius);

                    if (diff < wave.waveWidth) {
                        double lifetimeFactor = Math.max(0, 1.0 - (wave.radius / wave.options.maxRadius()));
                        double falloff = Math.sin((1.0 - diff / wave.waveWidth) * Math.PI);
                        double force = falloff * wave.intensity * lifetimeFactor;

                        if (dist > 0) {
                            totalOx += (dx / dist) * force;
                            totalOy += (dy / dist) * force;
                            active = true;
                        }
                    }
                }

                if (active) {
                    // Offset berechnen: Woher im Source-Image nehmen wir die Pixel?
                    // Wir nutzen x/y (lokal zur Area), addieren den Schockwellen-Versatz
                    int srcX = x + (int) totalOx;
                    int srcY = y + (int) totalOy;

                    // Sicherstellen, dass wir nicht außerhalb des source-Images lesen
                    srcX = Math.max(0, Math.min(srcX, w - tileSize));
                    srcY = Math.max(0, Math.min(srcY, h - tileSize));

                    target.drawImage(source,
                        worldX, worldY, worldX + tileSize, worldY + tileSize, // Ziel auf dem Screen
                        srcX, srcY, srcX + tileSize, srcY + tileSize,         // Quelle im Kamera-Bild
                        null);
                }
            }
        }
    }



}
