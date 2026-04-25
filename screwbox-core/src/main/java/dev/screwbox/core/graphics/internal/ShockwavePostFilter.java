package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.postfilter.PostProcessingContext;
import dev.screwbox.core.graphics.postfilter.PostProcessingFilter;
import dev.screwbox.core.utils.Validate;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class ShockwavePostFilter implements PostProcessingFilter {


    private final List<Shockwave> waves;
    private final int tileSize;

    ShockwavePostFilter(final List<Shockwave> waves, final int cellSize) {
        Validate.positive(cellSize, "cellSize must be positive");
        this.waves = waves;
        this.tileSize = cellSize;
    }

    record CalculatedWave(double radius, double maxRadius, Offset position, double width, double intensity) {

    }

    @Override
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        drawSourceImage(source, target, context);

        final List<CalculatedWave> calculatedWaves = calculateWaves(context.viewport());
        for (int y = 0; y < context.height(); y += tileSize) {
            for (int x = 0; x < context.width(); x += tileSize) {
                double totalOx = 0;
                double totalOy = 0;
                boolean active = false;

                final Offset absolute = context.bounds().offset().add(x, y);

                for (var wave : calculatedWaves) {
                    final Offset dist = absolute.substract(wave.position);
                    final double distance = Math.sqrt((double) dist.x() * dist.x() + dist.y() * dist.y());
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
                    final int srcX = Math.clamp(absolute.x() + (long) totalOx, context.bounds().x(), context.bounds().x() + context.width() - tileSize);
                    final int srcY = Math.clamp(absolute.y() + (long) totalOy, context.bounds().y(), context.bounds().y() + context.height() - tileSize);

                    target.drawImage(source,
                        absolute.x(), absolute.y(), absolute.x() + tileSize, absolute.y() + tileSize,
                        srcX, srcY, srcX + tileSize, srcY + tileSize,
                        null);
                }
            }
        }
    }

    private List<CalculatedWave> calculateWaves(final Viewport viewport) {
        final List<CalculatedWave> calculatedWaves = new ArrayList<>();
        for (final Shockwave wave : waves) {
            final int radius = viewport.toCanvas(wave.radius());
            final int maxRadius = viewport.toCanvas(wave.options().radius());
            final Offset position = viewport.toCanvas(wave.position()).add(viewport.canvas().offset());
            final int width = viewport.toCanvas(wave.waveWidth());
            final var waveBounds = calculateWaveBounds(position, radius, width);
            if (waveBounds.intersects(viewport.canvas().bounds())) {
                calculatedWaves.add(new CalculatedWave(radius, maxRadius, position, width, wave.intensity()));
            }
        }
        return calculatedWaves;
    }

    private ScreenBounds calculateWaveBounds(final Offset position, final int radius, final int width) {
        final Offset origin = position.add(-width - radius, -width - radius);
        return new ScreenBounds(origin, Size.square(radius * 2 + width * 2));
    }
}