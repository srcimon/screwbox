package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Path;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates wave positions and speeds of all {@link FluidComponent fluid components}.
 */
public class FluidSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidComponent.class);

    private static final double MAX_DELTA = 0.01;

    @Override
    public void update(final Engine engine) {
        for (final var fluidEntity : engine.environment().fetchAll(FLUIDS)) {
            final double fluidHeight = fluidEntity.bounds().height();
            final var fluid = fluidEntity.get(FluidComponent.class);

            double remainingDelta = engine.loop().delta();
            while (remainingDelta > 0) {
                final double delta = Math.min(MAX_DELTA, remainingDelta);
                updateHeights(fluid, delta, fluidHeight);
                updateSpeeds(fluid, delta);
                remainingDelta -= delta;
            }
            fluid.surface = createSurface(fluidEntity.bounds(), fluid);
        }
    }

    private Path createSurface(final Bounds bounds, final FluidComponent fluid) {
        Validate.min(fluid.nodeCount, 2, "fluid must have at least two nodes");
        final var gap = bounds.width() / (fluid.nodeCount - 1);
        final List<Vector> surface = new ArrayList<>();
        for (int i = 0; i < fluid.nodeCount; i++) {
            surface.add(bounds.origin().add(i * gap, fluid.height[i]));
        }
        return Path.withNodes(surface);
    }

    private void updateHeights(final FluidComponent fluid, final double delta, final double fluidHeight) {
        for (int i = 0; i < fluid.nodeCount; i++) {
            fluid.height[i] = Math.min(fluid.height[i] + delta * fluid.speed[i], fluidHeight);
        }
    }

    private void updateSpeeds(final FluidComponent fluid, final double delta) {
        for (int i = 0; i < fluid.nodeCount; i++) {
            // side pull
            final double deltaLeft = i > 0 ? fluid.height[i] - fluid.height[i - 1] : 0;
            final double deltaRight = i < fluid.nodeCount - 1 ? fluid.height[i] - fluid.height[i + 1] : 0;
            fluid.speed[i] -= delta * fluid.transmission * (deltaLeft + deltaRight);

            // retraction
            fluid.speed[i] -= fluid.height[i] * Math.min(1, fluid.retract * delta);

            // dampen
            fluid.speed[i] -= fluid.dampening * fluid.speed[i] * delta;
        }
    }
}