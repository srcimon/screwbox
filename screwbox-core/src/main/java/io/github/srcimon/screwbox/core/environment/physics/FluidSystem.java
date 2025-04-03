package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

/**
 * Updates wave positions and speeds of all {@link FluidComponent fluid components}.
 */
public class FluidSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.of(FluidComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var fluidEntity : engine.environment().fetchAll(FLUIDS)) {
            final var fluid = fluidEntity.get(FluidComponent.class);
            final double delta = Math.min(engine.loop().delta(), fluid.maxDelta);
            for (int i = 0; i < fluid.nodeCount; i++) {
                final double deltaLeft = i > 0 ? fluid.height[i] - fluid.height[i - 1] : 0;
                final double deltaRight = i < fluid.nodeCount - 1 ? fluid.height[i] - fluid.height[i + 1] : 0;
                final double sidePull = delta * fluid.transmission * (deltaLeft + deltaRight);
                final double retraction = fluid.height[i] * fluid.retract * delta;
                final double dampen = fluid.dampening * fluid.speed[i] * delta;
                fluid.height[i] += delta * fluid.speed[i];
                fluid.speed[i] += -sidePull - retraction - dampen;
            }
        }
    }

}
