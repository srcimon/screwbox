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
    private static final int ITERATIONS = 20;

    public ExperimentalPostFilter(Polygon outline, Polygon surface) {
        this.outline = outline;
        this.surface = surface;
    }
//TODO add reflection
    @Override
    public void apply(Image source, Graphics2D target, PostProcessingContext context) {
        drawSourceImage(source, target, context);

        final var area = context.bounds();
        final double scale = context.resolutionScale();
        final var originalClip = target.getClip();
        final double time = System.currentTimeMillis() / 800.0;

        // 1. Clipping (Schutz des Zielbereichs)
        List<Offset> outlineCanvasNodes = new ArrayList<>();
        for (var node : outline.definitionNotes()) {
            outlineCanvasNodes.add(context.viewport().toCanvas(node));
        }
        target.setClip(AwtMapper.toPath(outlineCanvasNodes));

        var rawSurfaceNodes = surface.definitionNotes();
        List<Offset> surfaceCanvasNodes = new ArrayList<>();
        for (int i = 0; i < rawSurfaceNodes.size() - 2; i++) {
            surfaceCanvasNodes.add(context.viewport().toCanvas(rawSurfaceNodes.get(i)));
        }

        // 2. Wellen-Loop: Stretching & Shifting
        for (int i = 0; i < ITERATIONS; i++) {
            double depthProgress = i / (double) ITERATIONS;
            double motionFactor = Math.sin(depthProgress * Math.PI);
            int segmentHeight = Math.max(1, context.height() / ITERATIONS);

            for (int j = 0; j < surfaceCanvasNodes.size(); j++) {
                var canvasNode = surfaceCanvasNodes.get(j);
                var worldNode = rawSurfaceNodes.get(j);

                // Basis-Phase (Kamera-Agnostisch)
                double phase = time + (worldNode.x() * 0.1) + (i * 0.55);

                // SHIFT (Versatz der Position)
                int shiftX = (int) (Math.sin(phase) * 15 * scale * motionFactor);
                int shiftY = (int) (Math.cos(phase * 1.2) * 8 * scale * motionFactor);

                // STRETCH (Verzerrung der Größe)
                int stretchX = (int) (Math.cos(phase) * 30 * scale * motionFactor);
                int stretchY = (int) (Math.sin(phase * 1.8) * 6 * scale * motionFactor);

                int sx = (int) canvasNode.x();
                int sy = (int) canvasNode.y();
                int currentY = sy + (i * segmentHeight);
                int segmentWidth = 120;

                // KOMBINATION:
                // Ziel-Rechteck wird verschoben (shift) UND in der Größe verändert (stretch).
                // Die Source bleibt stabil auf den Polygon-Pixeln fixiert.
                target.drawImage(source,
                    (int) (area.x() + sx + shiftX - stretchX / 2.0),
                    (int) (area.y() + currentY + shiftY - stretchY / 2.0),
                    (int) (area.x() + sx + segmentWidth + shiftX + stretchX / 2.0),
                    (int) (area.y() + currentY + segmentHeight + shiftY + stretchY / 2.0),
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
