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

        // 1. Clipping auf Outline (wie zuvor)
        List<Offset> outlineCanvasNodes = new ArrayList<>();
        for (var node : outline.definitionNotes()) {
            outlineCanvasNodes.add(context.viewport().toCanvas(node));
        }
        target.setClip(AwtMapper.toPath(outlineCanvasNodes));

        // 2. Surface Nodes für Welleneffekt vorbereiten (letzte zwei ignorieren)
        final var nodes = surface.definitionNotes();
        final int nodeCount = nodes.size() - 2;

        final double time = context.lifetime().milliseconds() / (double) interval.milliseconds();

        // 3. Distortion basierend auf Surface Nodes
        for (int i = 0; i < nodeCount; i++) {
            final Offset node = context.viewport().toCanvas(nodes.get(i));

            final double wave = Math.sin(time + (i * strength.value()));
            final int offset = (int) (wave * 12 * scale);

            // Quell-Koordinaten basierend auf der Knotenposition im Canvas
            final int sx = (int) node.x();
            final int sy = (int) node.y();

            // Wir nutzen eine feste Größe pro "Wellensegment" um den Knoten herum
            final int size = (int) (40 * scale);

            // Zeichne das verzerrte Segment
            target.drawImage(source,
                area.x() + sx - offset, area.y() + sy - offset, // Ziel oben links
                area.x() + sx + size + offset, area.y() + sy + size + offset, // Ziel unten rechts
                area.x() + sx, area.y() + sy, // Quelle oben links
                area.x() + sx + size, area.y() + sy + size, // Quelle unten rechts
                null);
        }

        target.setClip(originalClip);
    }
}
