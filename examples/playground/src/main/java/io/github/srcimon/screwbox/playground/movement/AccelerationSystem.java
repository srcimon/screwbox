package io.github.srcimon.screwbox.playground.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

public class AccelerationSystem implements EntitySystem {

    private static final Archetype ACCELERATORS = Archetype.of(AccelerationComponent.class, PhysicsComponent.class);


    @Override
    public void update(Engine engine) {
        for (final var accelerator : engine.environment().fetchAll(ACCELERATORS)) {
            var physics = accelerator.get(PhysicsComponent.class);
            var acceleration = accelerator.get(AccelerationComponent.class);

            physics.momentum = physics.momentum
                    .add(acceleration.acceleration.multiply(engine.loop().delta()))
                    .limitLength(acceleration.maxSpeed);
        }
    }
}
