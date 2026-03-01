package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.postfilter.PostProcessingContext;
import dev.screwbox.core.graphics.postfilter.PostProcessingFilter;

import java.awt.*;
import java.awt.image.VolatileImage;
import java.util.List;

import static dev.screwbox.core.Vector.$;

class ShockwavePostFilter implements PostProcessingFilter {


    private final List<Shockwave> waves;
    private final int tileSize;

    ShockwavePostFilter(final List<Shockwave> waves, int tileSize) {
        this.waves = waves;
        this.tileSize = tileSize;
    }

    @Override
    public void apply(final VolatileImage source, final Graphics2D target, final PostProcessingContext context) {
        final var area = context.bounds();
        final int w = area.size().width();
        final int h = area.size().height();
        final int offsetX = area.x();
        final int offsetY = area.y();

        // 1. Basisbild zeichnen: source (0,0 bis w,h) auf target (offsetX, offsetY)
        target.drawImage(source,
            area.x(), area.y(), area.maxX(), area.maxY(),
            area.x(), area.y(), area.maxX(), area.maxY(),
            null);

        for (int y = 0; y < h; y += tileSize) {
            for (int x = 0; x < w; x += tileSize) {

                double totalOx = 0;
                double totalOy = 0;
                boolean active = false;

                // Absolute Position auf dem gesamten Bildschirm/Target
                int screenX = offsetX + x;
                int screenY = offsetY + y;

                for (var wave : waves) {
                    double localRadius = context.viewport().toCanvas(wave.radius);
                    double localMaxRadius = context.viewport().toCanvas(wave.options.maxRadius());

                    // Schockwellen-Position von Welt- in Screen-Koordinaten umrechnen
                    var local = context.viewport().toCanvas($(wave.x, wave.y)).add(context.viewport().canvas().offset().x(), context.viewport().canvas().offset().y());
                    double dx = screenX - local.x();
                    double dy = screenY - local.y();
                    double distSq = dx * dx + dy * dy;
                    double dist = Math.sqrt(distSq);


                    double diff = Math.abs(dist - localRadius);

                    if (diff < wave.waveWidth) {
                        double lifetimeFactor = Math.max(0, 1.0 - (localRadius / localMaxRadius));
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
                    // WICHTIG: srcX/Y müssen hier ebenfalls den offsetX/Y enthalten,
                    // da das 'source' Bild im Split-Screen meist der geteilte Backbuffer ist.
                    int srcX = screenX + (int) totalOx;
                    int srcY = screenY + (int) totalOy;

                    // Clamping auf die Grenzen der aktuellen Kamera-Area,
                    // damit die Welle keine Pixel aus dem Nachbar-Screen klaut
                    srcX = Math.max(offsetX, Math.min(srcX, offsetX + w - tileSize));
                    srcY = Math.max(offsetY, Math.min(srcY, offsetY + h - tileSize));

                    target.drawImage(source,
                        screenX, screenY, screenX + tileSize, screenY + tileSize, // Wohin am Bildschirm
                        srcX, srcY, srcX + tileSize, srcY + tileSize,             // Woher aus dem Buffer
                        null);
                }
            }
        }
    }


}
