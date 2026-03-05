package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Viewport;
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

    record CalculatedWave(double radius, double maxRadius, Offset pos, double width, double intensity) {

    }

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        final var area = context.bounds();

        target.drawImage(source,
            area.x(), area.y(), area.maxX(), area.maxY(),
            area.x(), area.y(), area.maxX(), area.maxY(),
            null);

        final List<CalculatedWave> calculatedWaves = calculateWaves(context.viewport());
        for (int y = 0; y < context.height(); y += tileSize) {
            for (int x = 0; x < context.width(); x += tileSize) {
                double totalOx = 0;
                double totalOy = 0;
                boolean active = false;

                final Offset absolute = area.offset().add(x, y);

                for (var wave : calculatedWaves) {
                    final Offset dist = absolute.substract(wave.pos);
                    final double distance = Math.sqrt(dist.x() * dist.x() + dist.y() * dist.y());
                    final double diff = Math.abs(distance - wave.radius());

                    if (distance > 0 && diff < wave.width()) {
                        double lifetimeFactor = Math.max(0, 1.0 - (wave.radius() / wave.maxRadius()));
                        double falloff = Math.sin((1.0 - diff / wave.width()) * Math.PI);
                        double force = falloff * wave.intensity() * lifetimeFactor;
                        totalOx += (dist.x() / distance) * force;
                        totalOy += (dist.y() / distance) * force;
                        active = true;
                    }
                }

                if (active) {
                    final int srcX = Math.clamp(absolute.x() + (long) totalOx, area.x(), area.x() + context.width() - tileSize);
                    final int srcY = Math.clamp(absolute.y() + (long) totalOy, area.y(), area.y() + context.height() - tileSize);

                    target.drawImage(source,
                        absolute.x(), absolute.y(), absolute.x() + tileSize, absolute.y() + tileSize,
                        srcX, srcY, srcX + tileSize, srcY + tileSize,
                        null);
                }
            }
        }
    }

    private List<CalculatedWave> calculateWaves(final Viewport viewport) {
        List<CalculatedWave> calculatedWaves = new ArrayList<>(waves.size());
        for (final Shockwave wave : waves) {
            calculatedWaves.add(new CalculatedWave(
                viewport.toCanvas(wave.radius()),
                viewport.toCanvas(wave.options().radius()),
                viewport.toCanvas(wave.position()).add(viewport.canvas().offset()),
                viewport.toCanvas(wave.waveWidth()),
                wave.intensity()
            ));
        }
        return calculatedWaves;
    }


}
