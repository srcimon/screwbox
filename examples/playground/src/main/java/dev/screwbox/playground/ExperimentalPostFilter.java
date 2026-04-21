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
    private static final int ITERATIONS = 10;
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
        final double time = System.currentTimeMillis() / 800.0;

        // 1. Clipping auf Wasser-Areal
        List<Offset> outlineCanvasNodes = new ArrayList<>();
        for (var node : outline.definitionNotes()) {
            outlineCanvasNodes.add(context.viewport().toCanvas(node));
        }
        target.setClip(AwtMapper.toPath(outlineCanvasNodes));

        // 2. Surface Nodes (Welt-Koordinaten für Logik, Canvas für Position)
        var rawSurfaceNodes = surface.definitionNotes();
        List<Offset> surfaceCanvasNodes = new ArrayList<>();
        for (int i = 0; i < rawSurfaceNodes.size() - 2; i++) {
            surfaceCanvasNodes.add(context.viewport().toCanvas(rawSurfaceNodes.get(i)));
        }

        // 3. Wellen-Loop
        for (int i = 0; i < ITERATIONS; i++) {
            double depthProgress = i / (double) ITERATIONS;
            double motionFactor = Math.sin(depthProgress * Math.PI);
            int segmentHeight = Math.max(1, context.height() / ITERATIONS);

            for (int j = 0; j < surfaceCanvasNodes.size(); j++) {
                var canvasNode = surfaceCanvasNodes.get(j);
                var worldNode = rawSurfaceNodes.get(j);

                // Horizontale Phase & Offset
                double phaseX = time + (worldNode.x() * 0.01) + (i * 0.03);
                int offsetX = (int) (Math.sin(phaseX) * 40 * scale * motionFactor);

                // VERTIKALE BEWEGUNG: Nutzt eine verschobene Phase für organischen Look
                double phaseY = time * 1.2 + (worldNode.x() * 0.015) + (i * 0.05);
                int offsetY = (int) (Math.cos(phaseY) * 15 * scale * motionFactor);

                int sx = (int) canvasNode.x();
                int sy = (int) canvasNode.y();
                int currentY = sy + (i * segmentHeight);

                int segmentWidth = 120;

                // Zeichnen mit horizontalem UND vertikalem Offset
                target.drawImage(source,
                    (int) (area.x() + sx + offsetX),
                    (int) (area.y() + currentY + offsetY),
                    (int) (area.x() + sx + offsetX + segmentWidth),
                    (int) (area.y() + currentY + offsetY + segmentHeight),
                    sx,
                    currentY,
                    sx + segmentWidth,
                    currentY + segmentHeight,
                    null
                );
            }
        }

        target.setClip(originalClip);
    }
}
