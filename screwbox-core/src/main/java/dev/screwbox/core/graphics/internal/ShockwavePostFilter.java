package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.postfilter.PostProcessingContext;
import dev.screwbox.core.graphics.postfilter.PostProcessingFilter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class ShockwavePostFilter implements PostProcessingFilter {


    private final List<Shockwave> waves;
    private final int tileSize;

    ShockwavePostFilter(final List<Shockwave> waves, int tileSize) {
        this.waves = waves;
        this.tileSize = tileSize;
    }

    record LocalWave(double radius, double maxRadius, Offset pos, double width, double intensity) {

    }

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
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

        List<LocalWave> localWaves = new ArrayList<>(waves.size());
        for(final Shockwave wave : waves) {
            localWaves.add(new LocalWave(
                context.viewport().toCanvas(wave.radius()),
                context.viewport().toCanvas(wave.options().radius()),
                context.viewport().toCanvas(wave.position()).add(context.viewport().canvas().offset()),
                context.viewport().toCanvas(wave.waveWidth()),
                wave.intensity()//TODO why no translation?
            ));
        }
        for (int y = 0; y < h; y += tileSize) {
            for (int x = 0; x < w; x += tileSize) {

                double totalOx = 0;
                double totalOy = 0;
                boolean active = false;

                // Absolute Position auf dem gesamten Bildschirm/Target
                int screenX = offsetX + x;
                int screenY = offsetY + y;

                for (var wave : localWaves) {
                    double dx = screenX - wave.pos().x();
                    double dy = screenY - wave.pos().y();
                    double distSq = dx * dx + dy * dy;
                    double dist = Math.sqrt(distSq);


                    double diff = Math.abs(dist - wave.radius());

                    if (diff < wave.width()) {
                        double lifetimeFactor = Math.max(0, 1.0 - (wave.radius() / wave.maxRadius()));
                        double falloff = Math.sin((1.0 - diff / wave.width()) * Math.PI);
                        double force = falloff * wave.intensity() * lifetimeFactor;

                        if (dist > 0) {
                            totalOx += (dx / dist) * force;
                            totalOy += (dy / dist) * force;
                            active = true;
                        }
                    }
                }

                if (active) {
                    int srcX = screenX + (int) totalOx;
                    int srcY = screenY + (int) totalOy;

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
