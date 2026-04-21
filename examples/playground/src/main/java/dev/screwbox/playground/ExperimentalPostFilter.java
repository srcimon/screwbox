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
        // 1. Hintergrund einmalig zeichnen (das statische Bild)
        drawSourceImage(source, target, context);

        final var area = context.bounds();
        final double scale = context.resolutionScale();
        final var originalClip = target.getClip();

        // Zeit-Faktor (Millisekunden direkt nutzen für maximale Frequenz)
        final double time = context.lifetime().milliseconds() / 200.0;

        // 2. Clipping auf Wasser-Areal (damit wir nur dort "drübermalen")
        List<Offset> outlineCanvasNodes = new ArrayList<>();
        for (var node : outline.definitionNotes()) {
            outlineCanvasNodes.add(context.viewport().toCanvas(node));
        }
        target.setClip(AwtMapper.toPath(outlineCanvasNodes));

        // 3. Surface Nodes (Canvas-Koordinaten)
        List<Offset> surfaceCanvasNodes = new ArrayList<>();
        var rawSurfaceNodes = surface.definitionNotes();
        for (int i = 0; i < rawSurfaceNodes.size() - 2; i++) {
            surfaceCanvasNodes.add(context.viewport().toCanvas(rawSurfaceNodes.get(i)));
        }

        // 4. Wellen-Loop
        for (int i = 0; i < ITERATIONS; i++) {
            // Fortschritt von oben (0.0) nach unten (1.0)
            double depthProgress = i / (double) ITERATIONS;

            // Sinus-Faktor: 0 am Rand, 1 in der Mitte der Tiefe (stoppt Flackern an Kanten)
            double motionFactor = Math.sin(depthProgress * Math.PI);

            // Höhe eines Streifens berechnen
            int segmentHeight = Math.max(1, context.height() / ITERATIONS);

            for (var node : surfaceCanvasNodes) {
                // Die Phase ist für diesen horizontalen Punkt und diese Tiefe einzigartig
                double phase = time + (node.x() * 0.01) + (i * 0.3);
                int offset = (int) (Math.sin(phase) * 40 * scale * motionFactor );

                int sx = (int) node.x();
                int sy = (int) node.y();
                int currentY = sy + (i * segmentHeight);

                // WICHTIG: Große Breite, damit wir keine Lücken zwischen den Segmenten haben
                int segmentWidth = 120;

                // Wir zeichnen einen Teil des Quellbildes versetzt auf das Zielbild
                target.drawImage(source,
                    area.x() + sx + offset, area.y() + currentY, // Ziel (versetzt)
                    area.x() + sx + segmentWidth + offset, area.y() + currentY + segmentHeight,
                    area.x() + sx, area.y() + currentY, // Quelle (original)
                    area.x() + sx + segmentWidth, area.y() + currentY + segmentHeight,
                    null);
            }
        }
        target.setClip(originalClip);
    }
}
