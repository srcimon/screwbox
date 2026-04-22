package dev.screwbox.playground;

import dev.screwbox.core.Polygon;
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
    private static final int ITERATIONS = 30;

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
        final double time = System.currentTimeMillis() / 800.0;

// 1. Clipping auf die Outline setzen
        List<Offset> outlineNodes = outline.definitionNotes().stream()
            .map(context.viewport()::toCanvas).toList();
        Path2D outlinePath = AwtMapper.toPath(outlineNodes);
        Rectangle outlineBounds = outlinePath.getBounds();
        target.setClip(outlinePath);

        var rawSurfaceNodes = surface.definitionNotes();
        List<Offset> surfaceCanvasNodes = new ArrayList<>();
        for (int i = 0; i < rawSurfaceNodes.size(); i++) {
            surfaceCanvasNodes.add(context.viewport().toCanvas(rawSurfaceNodes.get(i)));
        }

        final int segmentHeight = Math.max(1, context.height() / ITERATIONS);

        for (int i = 0; i < ITERATIONS; i++) {
            double depthProgress = i / (double) ITERATIONS;
            double motionFactor = Math.sin(depthProgress * Math.PI);

            for (int j = 0; j < surfaceCanvasNodes.size(); j++) {
                var canvasNode = surfaceCanvasNodes.get(j);
                var worldNode = rawSurfaceNodes.get(j);

                double phase = time + (worldNode.x() * 0.1) + (i * 0.55);
                int shiftX = (int) (Math.sin(phase) * 15 * scale * motionFactor);
                int shiftY = (int) (Math.cos(phase * 1.2) * 8 * scale * motionFactor);

                // Diese Werte sorgen für den "Nice"-Faktor
                int stretchX = (int) (Math.cos(phase) * 30 * scale * motionFactor);
                int stretchY = (int) (Math.sin(phase * 1.8) * 6 * scale * motionFactor);

                int srcX = canvasNode.x();
                int srcY = canvasNode.y() + (i * segmentHeight);
                int segmentWidth = 120;

                // Quell-Koordinaten strikt auf Outline begrenzen (kein Kopieren von außen)
                int safeSrcX1 = Math.max(outlineBounds.x, Math.min(srcX, outlineBounds.x + outlineBounds.width));
                int safeSrcY1 = Math.max(outlineBounds.y, Math.min(srcY, outlineBounds.y + outlineBounds.height));
                int safeSrcX2 = Math.max(outlineBounds.x, Math.min(srcX + segmentWidth, outlineBounds.x + outlineBounds.width));
                int safeSrcY2 = Math.max(outlineBounds.y, Math.min(srcY + segmentHeight, outlineBounds.y + outlineBounds.height));

                if (safeSrcX2 > safeSrcX1 && safeSrcY2 > safeSrcY1) {
                    // FIX: stretchX/Y werden hier auf die Ziel-Koordinaten addiert
                    target.drawImage(source,
                        (int) (area.x() + srcX + shiftX - stretchX / 2.0),
                        (int) (area.y() + srcY + shiftY - stretchY / 2.0),
                        (int) (area.x() + srcX + (safeSrcX2 - safeSrcX1) + shiftX + stretchX / 2.0),
                        (int) (area.y() + srcY + (safeSrcY2 - safeSrcY1) + shiftY + stretchY / 2.0),
                        safeSrcX1, safeSrcY1,
                        safeSrcX2, safeSrcY2,
                        null
                    );
                }
            }
        }
        target.setClip(originalClip);
    }
}
