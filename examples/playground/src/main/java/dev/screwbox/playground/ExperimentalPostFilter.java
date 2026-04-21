package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.internal.AwtMapper;
import dev.screwbox.core.graphics.postfilter.PostProcessingContext;
import dev.screwbox.core.graphics.postfilter.PostProcessingFilter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExperimentalPostFilter implements PostProcessingFilter {

    private final Polygon outline;
    private final Polygon surface;
    Percent strength = Percent.of(0.8);
    private static final int ITERATIONS = 30;
    Duration interval = Duration.ofMillis(500);
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

        // 1. Clipping auf Wasser-Areal
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

        final double time = context.lifetime().milliseconds() / 1000.0; // Zeit in Sekunden

        // 3. Wasser-Simulation
        for (int i = 0; i < ITERATIONS; i++) {
            double depth = i / (double) ITERATIONS;
            double falloff = Math.pow(1.0 - depth, 1.5); // Schnelleres Abklingen in der Tiefe

            for (int n = 0; n < surfaceCanvasNodes.size(); n++) {
                final Offset node = surfaceCanvasNodes.get(n);

                // INTERFERENZ: Zwei Wellen überlagern sich für unregelmäßiges Muster
                // Welle 1: Schnell und klein (Oberflächenkräuseln)
                double wave1 = Math.sin(time * 3.5 + n * 0.5 + i * 0.2) * 12;
                // Welle 2: Langsam und groß (Hauptströmung/Woge)
                double wave2 = Math.sin(time * 1.2 - n * 0.2 + i * 0.5) * 25;

                double totalWave = (wave1 + wave2) * falloff * strength.value();
                int offset = (int) (totalWave * scale);

                final int sx = (int) node.x();
                final int sy = (int) node.y();

                // Vertikale Stauchung/Streckung für Lichtbrechungseffekt (Optional)
                int vOffset = (int) (Math.cos(time + n) * 3 * falloff);

                final int segmentHeight = Math.max(1, (context.height() / ITERATIONS));
                final int currentY = sy + (i * segmentHeight);
                final int segmentWidth = (int) (50 * scale);

                target.drawImage(source,
                    area.x() + sx + offset, area.y() + currentY + vOffset,
                    area.x() + sx + segmentWidth + offset, area.y() + currentY + segmentHeight + vOffset,
                    area.x() + sx, area.y() + currentY,
                    area.x() + sx + segmentWidth, area.y() + currentY + segmentHeight,
                    null);
            }
        }
        target.setClip(originalClip);
    }
}
