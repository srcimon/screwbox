package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;

@Order(SystemOrder.SIMULATION_BEGIN)
public class GravitySystem implements EntitySystem {

    private static final Archetype GRAVITY_AFFECTED = Archetype.of(PhysicsComponent.class);
    private static final Archetype GRAVITY = Archetype.of(GravityComponent.class);

    @Override
    public void update(Engine engine) {
        engine.environment().tryFetch(GRAVITY).ifPresent(gravityEntity -> {
            Vector gravity = gravityEntity.get(GravityComponent.class).gravity;
            Vector gravityDelta = gravity.multiply(engine.loop().delta());
            for (var entity : engine.environment().fetchAll(GRAVITY_AFFECTED)) {
                var physicsBodyComponent = entity.get(PhysicsComponent.class);
                physicsBodyComponent.momentum = physicsBodyComponent.momentum
                        .add(gravityDelta.multiply(physicsBodyComponent.gravityModifier));
            }
        });
    }
}