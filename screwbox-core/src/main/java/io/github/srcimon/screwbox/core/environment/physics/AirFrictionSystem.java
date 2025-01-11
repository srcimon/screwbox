package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.utils.MathUtil;

//TODO javadoc
//TODO changelog
//TODO test
public class AirFrictionSystem implements EntitySystem {

    private static final Archetype PHYSICS = Archetype.of(PhysicsComponent.class, AirFrictionComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var entity : engine.environment().fetchAll(PHYSICS)) {
            final var physicsBodyComponent = entity.get(PhysicsComponent.class);
            final double frictionX = entity.get(AirFrictionComponent.class).frictionX * engine.loop().delta();
            final double frictionY = entity.get(AirFrictionComponent.class).frictionY * engine.loop().delta();
            double absX = Math.abs(physicsBodyComponent.momentum.x());
            double absY = Math.abs(physicsBodyComponent.momentum.y());
            double changeX = Math.clamp(MathUtil.modifier(physicsBodyComponent.momentum.x()) * frictionX * -1, -absX, absX);
            double changeY = Math.clamp(MathUtil.modifier(physicsBodyComponent.momentum.y()) * frictionY * -1, -absY, absY);
            physicsBodyComponent.momentum = physicsBodyComponent.momentum.add(changeX, changeY);
        }
    }
}
