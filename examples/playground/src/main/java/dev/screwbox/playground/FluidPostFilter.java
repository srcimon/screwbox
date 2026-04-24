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
        final double time = System.currentTimeMillis() / 600.0;
        final int tSize = fluid.tileSize;

        List<Offset> outlineNodes = fluid.outline.definitionNotes().stream().map(context.viewport()::toCanvas).toList();
        Path2D outlinePath = AwtMapper.toPath(outlineNodes);
        Rectangle bounds = outlinePath.getBounds();

        target.setClip(outlinePath);
        target.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        var surfaceNodes = fluid.surface.definitionNotes().stream().map(context.viewport()::toCanvas).toList();
        double avgY = surfaceNodes.stream().mapToDouble(Offset::y).average().orElse(0.0);

        int startY = Math.max(0, (bounds.y / tSize) * tSize);
        int startX = Math.max(0, (bounds.x / tSize) * tSize);
        int endY = Math.min(context.height(), bounds.y + bounds.height + tSize);
        int endX = Math.min(context.width(), bounds.x + bounds.width + tSize);

        for (int y = startY; y < endY; y += tSize) {
            for (int x = startX; x < endX; x += tSize) {

                double[] offset = calculatePreciseOffset(x, y, tSize, time, context.viewport(), context, surfaceNodes, avgY);
                double dOffX = offset[0];
                double dOffY = offset[1];

                double damping = calculateDampening(tSize, x, dOffX, y, dOffY, outlinePath);

                int sX = x + (int) (dOffX * damping);
                int sY = y + (int) (dOffY * damping);

                target.drawImage(source, x, y, x + tSize, y + tSize,
                    sX, sY, sX + tSize, sY + tSize, null);
            }
        }
    }

    private static double calculateDampening(int tSize, int x, double dOffX, int y, double dOffY, Path2D outlinePath) {
        double damping = 1.0;
        double[] testPoints = {0, 0, tSize, 0, 0, tSize, tSize, tSize};

        for (int p = 0; p < testPoints.length; p += 2) {
            double tx = x + testPoints[p] + dOffX;
            double ty = y + testPoints[p + 1] + dOffY;

            if (!outlinePath.contains(tx, ty)) {
                damping = Math.min(damping, findMaxDamping(outlinePath, x + testPoints[p], y + testPoints[p + 1], dOffX, dOffY));
            }
        }
        return damping;
    }

    private static double findMaxDamping(Path2D path, double x, double y, double dx, double dy) {
        double low = 0.0, high = 1.0;
        for (int i = 0; i < 3; i++) {
            double mid = (low + high) / 2.0;
            if (path.contains(x + dx * mid, y + dy * mid)) low = mid;
            else high = mid;
        }
        return low;
    }

    private static double[] calculatePreciseOffset(int x, int y, int tSize, double time, Viewport viewport, PostProcessingContext context, List<Offset> surfaceNodes, double avgY) {
        Vector worldPos = viewport.toWorld(context.bounds().offset().add(x, y));
        int nodeIdx = Math.clamp(x / tSize, 0, surfaceNodes.size() - 1);
        var node = surfaceNodes.get(nodeIdx);

        double distToSurface = Math.abs((context.bounds().y() + y) - node.y());
        double decay = Math.max(0, 1.0 - (distToSurface / (context.height() * 0.8)));
        double wave = Math.abs(node.y() - avgY) * 0.15 * decay;

        double dx = (Math.sin(time * 0.5 + (worldPos.x() + worldPos.y()) * 0.05) * 3.0 + (Math.sin(time + worldPos.x() * 0.1) * wave)) * 8 * context.resolutionScale();
        double dy = Math.cos(time * 0.7 + worldPos.y() * 0.1) * (5 * context.resolutionScale() + wave * 10);
        return new double[]{dx, dy};
    }
}
