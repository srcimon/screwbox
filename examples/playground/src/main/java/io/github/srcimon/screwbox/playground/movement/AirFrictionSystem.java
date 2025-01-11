package io.github.srcimon.screwbox.playground.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.utils.MathUtil;

public class AirFrictionSystem implements EntitySystem {

    private static final Archetype PHYSICS = Archetype.of(PhysicsComponent.class, AirFrictionComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var entity : engine.environment().fetchAll(PHYSICS)) {
            final var physicsBodyComponent = entity.get(PhysicsComponent.class);
            //TODO friction y
            final double friction = entity.get(AirFrictionComponent.class).friction * engine.loop().delta();
            physicsBodyComponent.momentum = physicsBodyComponent.momentum.addX(MathUtil.modifier(physicsBodyComponent.momentum.x()) * friction * -1);
            System.out.println(physicsBodyComponent.momentum.x());
        }
    }
}
