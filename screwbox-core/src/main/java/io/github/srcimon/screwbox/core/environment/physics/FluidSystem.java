package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

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
        }
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