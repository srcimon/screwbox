package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Time;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.internal.AwtMapper;
import dev.screwbox.core.graphics.postfilter.PostProcessingContext;
import dev.screwbox.core.graphics.postfilter.PostProcessingFilter;
import dev.screwbox.core.utils.PerlinNoise;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExperimentalPostFilter implements PostProcessingFilter {

    private final Polygon outline;
    private final Polygon surface;
    Percent strength = Percent.of(0.8);
    private static final int ITERATIONS = 30;
    public ExperimentalPostFilter(Polygon outline, Polygon surface) {
        this.outline = outline;
        this.surface = surface;
    }

    @Override
    public void apply(Image source, Graphics2D target, PostProcessingContext context) {
        drawSourceImage(source, target, context);

        final var area = context.bounds();
        final double scale = context.resolutionScale();
        final var originalClip = target.getClip();
        final long time = context.lifetime().milliseconds();

        // 1. Clipping (Outline)
        List<Offset> outlineCanvasNodes = new ArrayList<>();
        for (var node : outline.definitionNotes()) {
            outlineCanvasNodes.add(context.viewport().toCanvas(node));
        }
        target.setClip(AwtMapper.toPath(outlineCanvasNodes));

        // 2. Surface Nodes
        List<Offset> surfaceCanvasNodes = new ArrayList<>();
        var rawSurfaceNodes = surface.definitionNotes();
        for (int i = 0; i < rawSurfaceNodes.size() - 2; i++) {
            surfaceCanvasNodes.add(context.viewport().toCanvas(rawSurfaceNodes.get(i)));
        }

        // 3. Perlin Noise Simulation mit Fokus auf Surface-Motion
        for (int i = 0; i < ITERATIONS; i++) {
            double depthProgress = i / (double) ITERATIONS;

            // Härterer Falloff für die Tiefe: Oberfläche dominiert massiv
            double falloff = Math.pow(1.0 - depthProgress, 2.0);

            final int segmentHeight = Math.max(1, (context.height() / ITERATIONS));

            for (int n = 0; n < surfaceCanvasNodes.size(); n++) {
                final Offset node = surfaceCanvasNodes.get(n);

                // Dynamik-Mix:
                // - n / 5.0 sorgt für weite, flüssige Wellen horizontal
                // - time * 0.003 beschleunigt die zeitliche Drift
                // - i * 0.1 reduziert die Unruhe in der Tiefe
                double noise = PerlinNoise.generatePerlinNoise3d(
                    42132,
                    n / 5.0,
                    i * 0.1,
                    time * 0.003
                );

                // Höhere Grundamplitude (40) für deutlichere Bewegung an der Oberfläche
                int offset = (int) (noise * 80 * scale * falloff * strength.value());

                final int sx = (int) node.x();
                final int sy = (int) node.y();
                final int currentY = sy + (i * segmentHeight);
                final int segmentWidth = (int) (80 * scale);

                target.drawImage(source,
                    area.x() + sx + offset, area.y() + currentY,
                    area.x() + sx + segmentWidth + offset, area.y() + currentY + segmentHeight,
                    area.x() + sx, area.y() + currentY,
                    area.x() + sx + segmentWidth, area.y() + currentY + segmentHeight,
                    null);

            }
        }
        target.setClip(originalClip);
    }
}
