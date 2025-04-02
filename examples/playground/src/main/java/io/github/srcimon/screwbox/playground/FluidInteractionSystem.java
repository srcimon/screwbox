package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

public class FluidInteractionSystem implements EntitySystem {

    private static final Archetype INTERACTORS = Archetype.ofSpacial(FluidInteractionComponent.class);
    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidComponent.class);

    @Override
    public void update(Engine engine) {
        final var fluids = engine.environment().fetchAll(FLUIDS);
        final var interactors = engine.environment().fetchAll(INTERACTORS);
        for (final var entity : fluids) {
            Fluid fluid = entity.get(FluidComponent.class).fluid;
            for (final var interactor : interactors) {
                final var irritation = interactor.get(PhysicsComponent.class).momentum.length();
                final var fluidInteraction = interactor.get(FluidInteractionComponent.class);
                if (Math.abs(irritation) > fluidInteraction.threshold) {
                    if (entity.bounds().intersects(interactor.bounds().expandTop(maxHeight(fluid)))) {
                        fluid.interact(entity.bounds(), interactor.bounds(), irritation * engine.loop().delta() * fluidInteraction.modifier);
                    }
                }
            }
        }
    }

    private double maxHeight(Fluid fluid) {
        double maxHeight = 0;
        for (int i = 0; i < fluid.nodeCount(); i++) {
            var height = fluid.getHeight(i);
            if (height > maxHeight) {
                maxHeight = height;
            }
        }
        return maxHeight;
    }
}
