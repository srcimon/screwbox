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
        // 1. Hintergrund einmalig zeichnen
        drawSourceImage(source, target, context);

        final var area = context.bounds();
        final double scale = context.resolutionScale();
        final var originalClip = target.getClip();

// Zeit-Faktor für die Animation
        final double time = System.currentTimeMillis() / 800.0;

// 2. Clipping auf Wasser-Areal (Welt -> Canvas Transformation)
        List<Offset> outlineCanvasNodes = new ArrayList<>();
        for (var node : outline.definitionNotes()) {
            outlineCanvasNodes.add(context.viewport().toCanvas(node));
        }
        target.setClip(AwtMapper.toPath(outlineCanvasNodes));

// 3. Surface Nodes vorbereiten
// Wir brauchen die Welt-Koordinaten für die Berechnung und Canvas für das Zeichnen
        var rawSurfaceNodes = surface.definitionNotes();
        List<Offset> surfaceCanvasNodes = new ArrayList<>();
        for (int i = 0; i < rawSurfaceNodes.size() - 2; i++) {
            surfaceCanvasNodes.add(context.viewport().toCanvas(rawSurfaceNodes.get(i)));
        }

// 4. Wellen-Loop
        final int ITERATIONS = 15; // Beispielwert, falls nicht definiert
        for (int i = 0; i < ITERATIONS; i++) {
            double depthProgress = i / (double) ITERATIONS;

            // Sinus-Faktor verhindert Flackern an den Rändern
            double motionFactor = Math.sin(depthProgress * Math.PI);
            int segmentHeight = Math.max(1, context.height() / ITERATIONS);

            for (int j = 0; j < surfaceCanvasNodes.size(); j++) {
                var canvasNode = surfaceCanvasNodes.get(j);
                var worldNode = rawSurfaceNodes.get(j);

                // AGNOSTIC TO CAMERA: Phase berechnet sich aus Welt-X
                // worldNode.x() bleibt konstant, auch wenn die Kamera scrollt
                double phase = time + (worldNode.x() * 0.01) + (i * 0.03);
                int offset = (int) (Math.sin(phase) * 40 * scale * motionFactor);

                int sx = (int) canvasNode.x();
                int sy = (int) canvasNode.y();
                int currentY = sy + (i * segmentHeight);

                // Segment-Breite für Überlappung
                int segmentWidth = 120;

                // Zeichnen des versetzten Bildteils
                target.drawImage(source,
                    (int)(area.x() + sx + offset),
                    (int)(area.y() + currentY),
                    (int)(area.x() + sx + offset + segmentWidth),
                    (int)(area.y() + currentY + segmentHeight),
                    sx,
                    currentY,
                    sx + segmentWidth,
                    currentY + segmentHeight,
                    null
                );
            }
        }

// Clipping zurücksetzen
        target.setClip(originalClip);
    }
}
