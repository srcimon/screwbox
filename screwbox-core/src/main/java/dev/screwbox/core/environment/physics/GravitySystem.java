package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;

@Order(Order.SystemOrder.SIMULATION_EARLY)
public class GravitySystem implements EntitySystem {

    private static final Archetype GRAVITY_AFFECTED = Archetype.of(PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        engine.environment().tryFetchSingleton(GravityComponent.class).ifPresent(gravityEntity -> {
            final Vector gravity = gravityEntity.get(GravityComponent.class).gravity;
            final Vector gravityDelta = gravity.multiply(engine.loop().delta());
            for (final var entity : engine.environment().fetchAll(GRAVITY_AFFECTED)) {
                var physicsBodyComponent = entity.get(PhysicsComponent.class);
                physicsBodyComponent.momentum = physicsBodyComponent.momentum
                        .add(gravityDelta.multiply(physicsBodyComponent.gravityModifier));
            }
        });
    }
}