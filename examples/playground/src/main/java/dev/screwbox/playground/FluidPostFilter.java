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
    public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
        drawSourceImage(source, target, context);

        for (final var fluid : fluids) {
            final Bounds fluidBounds = Bounds.around(fluid.outline.definitionNotes());
            if (fluidBounds.intersects(context.viewport().visibleArea())) {
                renderFluid(source, target, context, fluid);
            }
        }
    }

    private static void renderFluid(Image source, Graphics2D target, PostProcessingContext context, Fluid fluid) {
        final double time = System.currentTimeMillis() / 600.0;
        final int tSize = fluid.tileSize;
        final Viewport vp = context.viewport();

        List<Offset> outlineNodes = fluid.outline.definitionNotes().stream().map(vp::toCanvas).map(o -> o.add(context.viewport().canvas().offset())).toList();
        Path2D outlinePath = AwtMapper.toPath(outlineNodes);
        Rectangle bounds = outlinePath.getBounds();

        target.setClip(outlinePath);
        var surfaceNodes = fluid.surface.definitionNotes().stream().map(vp::toCanvas).toList();
        double avgY = surfaceNodes.stream().mapToDouble(Offset::y).average().orElse(0.0);

        for (int y = (bounds.y / tSize) * tSize; y < bounds.y + bounds.height; y += tSize) {
            for (int x = (bounds.x / tSize) * tSize; x < bounds.x + bounds.width; x += tSize) {

                Vector off = calculatePreciseOffset(x, y, tSize, time, vp, context, surfaceNodes, avgY);

                double damping = 1.0;
                double targetX = x + off.x();
                double targetY = y + off.y();

                if (targetX < bounds.x || targetX + tSize > bounds.x + bounds.width || targetY + tSize > bounds.y + bounds.height) {
                    damping = 0.0;
                } else {
                    double preciseSurfaceY = getInterpolatedY(targetX + tSize / 2.0, surfaceNodes, tSize);

                    if (targetY < preciseSurfaceY) {
                        // Sanfter Übergang (Damping-Zone von 8 Pixeln)
                        damping = Math.clamp(1.0 - (preciseSurfaceY - targetY) / 8.0, 0.0, 1.0);
                    }
                }

                int sX = x + (int) (off.x() * damping);
                int sY = y + (int) (off.y() * damping);

                target.drawImage(source, x, y, x + tSize, y + tSize, sX, sY, sX + tSize, sY + tSize, null);
            }
        }
    }

    private static double getInterpolatedY(double screenX, List<Offset> nodes, int tSize) {
        double floatIdx = screenX / tSize;
        int idxA = Math.clamp((int) floatIdx, 0, nodes.size() - 1);
        int idxB = Math.clamp(idxA + 1, 0, nodes.size() - 1);

        double t = floatIdx - idxA;
        return nodes.get(idxA).y() * (1.0 - t) + nodes.get(idxB).y() * t;
    }

    private static Vector calculatePreciseOffset(int x, int y, int tSize, double time, Viewport viewport, PostProcessingContext context, List<Offset> surfaceNodes, double avgY) {
        Vector worldPos = viewport.toWorld(context.bounds().offset().add(x, y));
        int nodeIdx = Math.clamp(x / tSize, 0, surfaceNodes.size() - 1);
        var node = surfaceNodes.get(nodeIdx);

        double distToSurface = Math.abs((context.bounds().y() + y) - node.y());
        double decay = Math.max(0, 1.0 - (distToSurface / (context.height() * 0.8)));
        double wave = Math.abs(node.y() - avgY) * 0.15 * decay;

        double dx = (Math.sin(time * 0.5 + (worldPos.x() + worldPos.y()) * 0.05) * 3.0 + (Math.sin(time + worldPos.x() * 0.1) * wave)) * 8 * context.resolutionScale();
        double dy = Math.cos(time * 0.7 + worldPos.y() * 0.1) * (5 * context.resolutionScale() + wave * 10);
        return Vector.of(dx, dy);
    }
}
