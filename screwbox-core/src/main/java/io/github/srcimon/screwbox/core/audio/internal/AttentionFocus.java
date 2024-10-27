package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.graphics.internal.ViewportManager;

public class AttentionFocus {

    private final ViewportManager viewportManager;

    public AttentionFocus(final ViewportManager viewportManager) {
        this.viewportManager = viewportManager;
    }

    public double distanceTo(final Vector position) {
        var minDistance = Double.MAX_VALUE;
        for (var viewport : viewportManager.activeViewports()) {
            final var distance = viewport.camera().position().distanceTo(position);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        return minDistance;
    }

    public Vector direction(final Vector position) {
        final Vector direction = Vector.zero();
        for (var viewport : viewportManager.activeViewports()) {
            direction.add(position.substract(viewport.camera().position()));

        }
        return direction.length(1);
    }

}
