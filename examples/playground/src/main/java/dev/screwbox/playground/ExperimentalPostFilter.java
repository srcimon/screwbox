package dev.screwbox.playground;

import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.internal.AwtMapper;
import dev.screwbox.core.graphics.postfilter.PostProcessingContext;
import dev.screwbox.core.graphics.postfilter.PostProcessingFilter;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class ExperimentalPostFilter implements PostProcessingFilter {

    private final Polygon outline;
    private final Polygon surface;

    public ExperimentalPostFilter(Polygon outline, Polygon surface) {
        this.outline = outline;
        this.surface = surface;
    }

    @Override
    public void apply(Image source, Graphics2D target, PostProcessingContext context) {
        drawSourceImage(source, target, context);

        final var area = context.bounds();
        final double scale = context.resolutionScale();
        final double time = System.currentTimeMillis() / 600.0;
        final int tileSize = 8;
        final var viewport = context.viewport();

// Outline & Bounds
        List<Offset> outlineNodes = outline.definitionNotes().stream()
            .map(viewport::toCanvas).toList();
        Path2D outlinePath = AwtMapper.toPath(outlineNodes);
        Rectangle outlineBounds = outlinePath.getBounds();

        target.setClip(outlinePath);

// Oberflächen-Nodes in Canvas-Koordinaten
        var surfaceNodes = surface.definitionNotes().stream()
            .map(viewport::toCanvas).toList();

// 1. Mittelwert (Durchschnitt) der Y-Koordinaten berechnen
        double avgY = surfaceNodes.stream()
            .mapToDouble(Offset::y)
            .average()
            .orElse(0.0);

        for (int y = 0; y < context.height(); y += tileSize) {
            for (int x = 0; x < context.width(); x += tileSize) {

                Vector worldPos = viewport.toWorld(Offset.at(area.x() + x, area.y() + y));
                double worldX = worldPos.x();
                double worldY = worldPos.y();

                int nodeIdx = Math.clamp(x / tileSize, 0, surfaceNodes.size() - 1);
                var node = surfaceNodes.get(nodeIdx);

                // 2. Wellenhöhe basierend auf Abweichung zum Mittelwert
                double waveHeight = Math.abs(node.y() - avgY) * 0.1;

                double ambient = Math.sin(time * 0.5 + (worldX + worldY) * 0.05) * 3.0;
                double force = ambient + (Math.sin(time + worldX * 0.1) * waveHeight);

                double desiredOffX = force * 8 * scale;
                double desiredOffY = Math.cos(time * 0.7 + worldY * 0.1) * (5 + waveHeight) * scale;

                // Anti-Flicker Damping
                double damping = 1.0;
                for (int i = 1; i <= 3; i++) {
                    double f = i / 3.0;
                    if (!outlinePath.contains(area.x() + x + desiredOffX * f, area.y() + y + desiredOffY * f)) {
                        damping = (i - 1.0) / 3.0;
                        break;
                    }
                }

                int finalOffX = (int) (desiredOffX * damping);
                int finalOffY = (int) (desiredOffY * damping);

                // Source-Cropping
                int sX1 = area.x() + x + finalOffX;
                int sY1 = area.y() + y + finalOffY;
                int sX2 = sX1 + tileSize;
                int sY2 = sY1 + tileSize;

                int csX1 = Math.max(outlineBounds.x, Math.min(sX1, outlineBounds.x + outlineBounds.width));
                int csY1 = Math.max(outlineBounds.y, Math.min(sY1, outlineBounds.y + outlineBounds.height));
                int csX2 = Math.max(outlineBounds.x, Math.min(sX2, outlineBounds.x + outlineBounds.width));
                int csY2 = Math.max(outlineBounds.y, Math.min(sY2, outlineBounds.y + outlineBounds.height));

                int dx1 = csX1 - sX1; int dy1 = csY1 - sY1;
                int dx2 = csX2 - sX2; int dy2 = csY2 - sY2;

                if (csX2 > csX1 && csY2 > csY1) {
                    target.drawImage(source,
                        area.x() + x + dx1, area.y() + y + dy1,
                        area.x() + x + tileSize + dx2, area.y() + y + tileSize + dy2,
                        csX1, csY1, csX2, csY2,
                        null);
                }
            }
        }
        target.setClip(null);
    }
}
