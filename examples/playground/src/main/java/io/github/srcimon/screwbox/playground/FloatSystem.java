package io.github.srcimon.screwbox.playground;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

public class FloatSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidComponent.class);
    private static final Archetype FLOATINGS = Archetype.ofSpacial(FloatComponent.class, PhysicsComponent.class);


    @Override
    public void update(Engine engine) {
        final var fluids = engine.environment().fetchAll(FLUIDS);
        final var floatings = engine.environment().fetchAll(FLOATINGS);

        for(final var fluid : fluids) {
            for(final var floating : floatings) {
                if (fluid.bounds().intersects(floating.bounds())) {
                    var physics = floating.get(PhysicsComponent.class);
                    physics.momentum = physics.momentum.addY(engine.loop().delta(-800));
                }
            }
        }
    }
}
