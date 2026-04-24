package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.internal.AwtMapper;
import dev.screwbox.core.graphics.postfilter.PostProcessingContext;
import dev.screwbox.core.graphics.postfilter.PostProcessingFilter;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.List;

public class FluidPostFilter implements PostProcessingFilter {

    public record Fluid(Polygon outline, Polygon surface, int tileSize) {

    }

    private List<Fluid> fluids;

    public FluidPostFilter(List<Fluid> fluids) {
        this.fluids = fluids;
    }

    @Override
    public void apply(Image source, Graphics2D target, PostProcessingContext context) {
        drawSourceImage(source, target, context);

        final double time = System.currentTimeMillis() / 600.0;

        final var viewport = context.viewport();

        for (final var fluid : fluids) {
            if (Bounds.around(fluid.outline.definitionNotes()).intersects(viewport.visibleArea())) {
                // Outline & Bounds
                List<Offset> outlineNodes = fluid.outline.definitionNotes().stream()
                    .map(viewport::toCanvas).toList();
                Path2D outlinePath = AwtMapper.toPath(outlineNodes);
                Rectangle outlineBounds = outlinePath.getBounds();

                target.setClip(outlinePath);

                var surfaceNodes = fluid.surface.definitionNotes().stream()
                    .map(viewport::toCanvas).toList();

                double avgY = surfaceNodes.stream()
                    .mapToDouble(Offset::y)
                    .average()
                    .orElse(0.0);
                for (int y = 0; y < context.height(); y += fluid.tileSize) {
                    for (int x = 0; x < context.width(); x += fluid.tileSize) {

                        Vector worldPos = viewport.toWorld(context.bounds().offset().add(x, y));

                        // 1. Lokalen Node finden
                        int nodeIdx = Math.clamp(x / fluid.tileSize, 0, surfaceNodes.size() - 1);
                        var node = surfaceNodes.get(nodeIdx);

                        // 2. Vertikale Distanz zur Oberfläche berechnen (Dämpfung nach unten)
                        // Je weiter y vom Oberflächen-Node entfernt ist, desto geringer der Effekt
                        double distToSurface = Math.abs((context.bounds().y() + y) - node.y());
                        double verticalDecay = Math.max(0, 1.0 - (distToSurface / (context.height() * 0.8)));

                        // 3. Lokale Wellenhöhe kombiniert mit Dämpfung
                        double localWaveImpact = Math.abs(node.y() - avgY) * 0.15 * verticalDecay;

                        // Hintergrund-Rauschen (Ambient) bleibt immer leicht aktiv
                        double ambient = Math.sin(time * 0.5 + (worldPos.x() + worldPos.y()) * 0.05) * 3.0;
                        double force = ambient + (Math.sin(time + worldPos.x() * 0.1) * localWaveImpact);

                        // Effektstärke
                        double desiredOffX = force * 8 * context.resolutionScale();
                        double desiredOffY = Math.cos(time * 0.7 + worldPos.y() * 0.1) * (5 * context.resolutionScale() + localWaveImpact * 10);

                        // Anti-Flicker Damping
                        double damping = 1.0;
                        for (int i = 1; i <= 3; i++) {
                            double f = i / 3.0;
                            if (!outlinePath.contains(context.bounds().x() + x + desiredOffX * f, context.bounds().y() + y + desiredOffY * f)) {
                                damping = (i - 1.0) / 3.0;
                                break;
                            }
                        }

                        // Source-Cropping
                        int sX1 = context.bounds().x() + x + (int) (desiredOffX * damping);
                        int sY1 = context.bounds().y() + y + (int) (desiredOffY * damping);
                        int sX2 = sX1 + fluid.tileSize;
                        int sY2 = sY1 + fluid.tileSize;

                        int csX1 = Math.clamp(sX1, outlineBounds.x, outlineBounds.x + outlineBounds.width);
                        int csY1 = Math.clamp(sY1, outlineBounds.y, outlineBounds.y + outlineBounds.height);
                        int csX2 = Math.clamp(sX2, outlineBounds.x, outlineBounds.x + outlineBounds.width);
                        int csY2 = Math.clamp(sY2, outlineBounds.y, outlineBounds.y + outlineBounds.height);

                        int dx1 = csX1 - sX1;
                        int dy1 = csY1 - sY1;
                        int dx2 = csX2 - sX2;
                        int dy2 = csY2 - sY2;

                        if (csX2 > csX1 && csY2 > csY1) {
                            target.drawImage(source,
                                context.bounds().x() + x + dx1, context.bounds().y() + y + dy1,
                                context.bounds().x() + x + fluid.tileSize + dx2, context.bounds().y() + y + fluid.tileSize + dy2,
                                csX1, csY1, csX2, csY2,
                                null);
                        }
                    }
                }
            }
        }
    }
}
