package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

public class FluidSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidComponent.class);

    @Override
    public void update(Engine engine) {
        final double delta = engine.loop().delta();
        for (final var fluidEntity : engine.environment().fetchAll(FLUIDS)) {
            final FluidComponent fluid = fluidEntity.get(FluidComponent.class);
            for (int i = 0; i < fluid.nodeCount; i++) {
                final double deltaLeft = i > 0 ? fluid.height[i] - fluid.height[i - 1] : 0;
                final double deltaRight = i < fluid.nodeCount - 1 ? fluid.height[i] - fluid.height[i + 1] : 0;
                fluid.height[i] += delta * fluid.speed[i];

                final double sidePull = delta * fluid.transmission * (deltaLeft + deltaRight);
                final double retraction = fluid.height[i] * fluid.retract * delta;
                final double dampen = fluid.dampening * fluid.speed[i] * delta;
                fluid.speed[i] += -sidePull - retraction - dampen;
            }
        }
    }

}
