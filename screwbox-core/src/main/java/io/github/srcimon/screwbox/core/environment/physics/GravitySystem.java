package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.*;

@Order(SystemOrder.SIMULATION_BEGIN)
public class GravitySystem implements EntitySystem {

    private static final Archetype GRAVITY_AFFECTED = Archetype.of(RigidBodyComponent.class);
    private static final Archetype GRAVITY = Archetype.of(GravityComponent.class);

    @Override
    public void update(Engine engine) {
        Entity gravityEntity = engine.environment().forcedFetch(GRAVITY);
        Vector gravity = gravityEntity.get(GravityComponent.class).gravity;
        Vector gravityDelta = gravity.multiply(engine.loop().delta());
        for (var entity : engine.environment().fetchAll(GRAVITY_AFFECTED)) {
            var physicsBodyComponent = entity.get(RigidBodyComponent.class);
            physicsBodyComponent.momentum = physicsBodyComponent.momentum
                    .add(gravityDelta.multiply(physicsBodyComponent.gravityModifier));
        }
    }
}