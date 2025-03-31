package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

public class FluidInteractionSystem implements EntitySystem {

    private static final Archetype INTERACTORS = Archetype.ofSpacial(FluidInteractionComponent.class);
    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidComponent.class);
    //TODO interact with hole body
    @Override
    public void update(Engine engine) {
        final var fluids = engine.environment().fetchAll(FLUIDS);
        final var interactors = engine.environment().fetchAll(INTERACTORS);
        for (final var entity : fluids) {
            Fluid fluid = entity.get(FluidComponent.class).fluid;
            for (final var interactor : interactors) {
                var irritation = interactor.get(PhysicsComponent.class).momentum.length();
                if (entity.bounds().intersects(interactor.bounds().expandTop(fluid.maxHeight()))) {
                    fluid.interact(entity.bounds(), interactor.bounds(), irritation * engine.loop().delta() * 4);
                }
            }
        }
    }
}
