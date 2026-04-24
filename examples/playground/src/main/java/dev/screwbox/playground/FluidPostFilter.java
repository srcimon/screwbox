package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Viewport;
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

        for (final var fluid : fluids) {
            final var visibleArea = context.viewport().visibleArea();
            if (Bounds.around(fluid.outline.definitionNotes()).intersects(visibleArea)) {
                renderFluid(source, target, context, fluid);
            }
        }
    }

    private static void renderFluid(Image source, Graphics2D target, PostProcessingContext context, Fluid fluid) {
        Viewport viewport = context.viewport();
        final double time = System.currentTimeMillis() / 600.0;

        // 1. Pfad und Bounds holen
        List<Offset> outlineNodes = fluid.outline.definitionNotes().stream().map(viewport::toCanvas).toList();
        Path2D outlinePath = AwtMapper.toPath(outlineNodes);
        Rectangle bounds = outlinePath.getBounds();

        // 2. Grafik-Clip auf das Polygon einschränken (verhindert Übermalen)
        Shape oldClip = target.getClip();
        target.setClip(outlinePath);

        var surfaceNodes = fluid.surface.definitionNotes().stream().map(viewport::toCanvas).toList();
        double avgY = surfaceNodes.stream().mapToDouble(Offset::y).average().orElse(0.0);

        // 3. Nur innerhalb der Bounding-Box loopen (massiver Performance-Gewinn)
        // Wir runden auf tileSize ab/auf, um das Raster sauber zu treffen
        int startY = Math.max(0, (bounds.y / fluid.tileSize) * fluid.tileSize);
        int endY = Math.min(context.height(), ((bounds.y + bounds.height) / fluid.tileSize + 1) * fluid.tileSize);
        int startX = Math.max(0, (bounds.x / fluid.tileSize) * fluid.tileSize);
        int endX = Math.min(context.width(), ((bounds.x + bounds.width) / fluid.tileSize + 1) * fluid.tileSize);

        for (int y = startY; y < endY; y += fluid.tileSize) {
            for (int x = startX; x < endX; x += fluid.tileSize) {

                // 4. Keine komplexen 'contains' Checks in der Schleife!
                // Das Clipping von Graphics2D erledigt das "Abschneiden" am Rand pixelgenau.

                Vector worldPos = viewport.toWorld(context.bounds().offset().add(x, y));
                int nodeIdx = Math.clamp(x / fluid.tileSize, 0, surfaceNodes.size() - 1);
                var node = surfaceNodes.get(nodeIdx);

                double distToSurface = Math.abs((context.bounds().y() + y) - node.y());
                double verticalDecay = Math.max(0, 1.0 - (distToSurface / (context.height() * 0.8)));
                double localWaveImpact = Math.abs(node.y() - avgY) * 0.15 * verticalDecay;

                double ambient = Math.sin(time * 0.5 + (worldPos.x() + worldPos.y()) * 0.05) * 3.0;
                double force = ambient + (Math.sin(time + worldPos.x() * 0.1) * localWaveImpact);

                double dOffX = force * 8 * context.resolutionScale();
                double dOffY = Math.cos(time * 0.7 + worldPos.y() * 0.1) * (5 * context.resolutionScale() + localWaveImpact * 10);

                // 5. Source-Koordinaten (woher wir Pixel nehmen)
                int sX = x + (int) dOffX;
                int sY = y + (int) dOffY;

                // 6. Zeichnen (Clip sorgt für saubere Kanten)
                target.drawImage(source, x, y, x + fluid.tileSize, y + fluid.tileSize,
                    sX, sY, sX + fluid.tileSize, sY + fluid.tileSize, null);
            }
        }

        target.setClip(oldClip);
    }
}
