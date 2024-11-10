package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Vector;

public class AttentionFocus {

    private final ViewportManager viewportManager;

    public AttentionFocus(final ViewportManager viewportManager) {
        this.viewportManager = viewportManager;
    }

    public double distanceTo(final Vector position) {
        var minDistance = Double.MAX_VALUE;
        for (var viewport : viewportManager.viewports()) {
            final var distance = viewport.camera().position().distanceTo(position);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        return minDistance;
    }

    public Vector direction(final Vector position, final double searchDistance) {
        Vector direction = Vector.zero();
        for (var viewport : viewportManager.viewports()) {
            if (viewport.visibleArea().expand(searchDistance).contains(position)) {
                Vector substract = position.substract(viewport.camera().position()).length(1);
                direction = direction.add(substract);
            }
        }
        return direction.length(1);
    }

    public boolean isWithinDistanceToVisibleArea(final Vector position, final double distance) {
        for (var viewport : viewportManager.viewports()) {
            if (viewport.visibleArea().expand(distance).contains(position)) {
                return true;
            }
        }
        return false;
    }
}
