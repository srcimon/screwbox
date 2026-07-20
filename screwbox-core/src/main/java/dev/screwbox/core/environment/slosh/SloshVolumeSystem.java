package dev.screwbox.core.environment.slosh;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates wave positions and speeds of all {@link SloshVolumeComponent slosh components}.
 */
public class SloshVolumeSystem implements EntitySystem {

    private static final Archetype SLOSH = Archetype.ofSpacial(SloshVolumeComponent.class);

    private static final double MAX_DELTA = 0.01;

    @Override
    public void update(final Engine engine) {
        for (final var sloshEntity : engine.environment().fetchAll(SLOSH)) {
            final double liquidHeight = sloshEntity.bounds().height();
            final var slosh = sloshEntity.get(SloshVolumeComponent.class);

            double remainingDelta = engine.loop().delta();
            while (remainingDelta > 0) {
                final double delta = Math.min(MAX_DELTA, remainingDelta);
                updateHeights(slosh, delta, liquidHeight);
                updateSpeeds(slosh, delta);
                remainingDelta -= delta;
            }
            slosh.surface = createSurface(sloshEntity.bounds(), slosh);
            slosh.outline = slosh.surface.addNodes(sloshEntity.bounds().bottomRight(), sloshEntity.bounds().bottomLeft());
        }
    }

    private static Polygon createSurface(final Bounds bounds, final SloshVolumeComponent slosh) {
        Validate.min(slosh.nodeCount, 2, "liquid must have at least two nodes");
        final var gap = bounds.width() / (slosh.nodeCount - 1);
        final List<Vector> surface = new ArrayList<>();
        for (int i = 0; i < slosh.nodeCount; i++) {
            surface.add(bounds.origin().add(i * gap, slosh.height[i]));
        }
        return Polygon.ofNodes(surface);
    }

    private void updateHeights(final SloshVolumeComponent slosh, final double delta, final double liquidHeight) {
        for (int i = 0; i < slosh.nodeCount; i++) {
            slosh.height[i] = Math.min(slosh.height[i] + delta * slosh.speed[i], liquidHeight);
        }
    }

    private void updateSpeeds(final SloshVolumeComponent slosh, final double delta) {
        for (int i = 0; i < slosh.nodeCount; i++) {
            // side pull
            final double deltaLeft = i > 0 ? slosh.height[i] - slosh.height[i - 1] : 0;
            final double deltaRight = i < slosh.nodeCount - 1 ? slosh.height[i] - slosh.height[i + 1] : 0;
            slosh.speed[i] -= delta * slosh.transmission * (deltaLeft + deltaRight);

            // retraction
            slosh.speed[i] -= slosh.height[i] * Math.min(1, slosh.retract * delta);

            // dampen
            slosh.speed[i] -= slosh.dampening * slosh.speed[i] * delta;
        }
    }
}