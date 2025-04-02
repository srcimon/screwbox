package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;

import java.util.ArrayList;
import java.util.List;

@Deprecated
//TODO me no liky
public class FluidSupport {

    public static List<Vector> calculateSurface(final Bounds bounds, final Fluid fluid) {
        final var gap = bounds.width() / (fluid.nodeCount - 1);
        final List<Vector> surfaceNodes = new ArrayList<>();
        for (int i = 0; i < fluid.nodeCount; i++) {
            surfaceNodes.add(bounds.origin().addX(i * gap).addY(fluid.height[i]));
        }
        surfaceNodes.add(bounds.bottomRight());
        surfaceNodes.add(bounds.bottomLeft());
        return surfaceNodes;
    }

}
