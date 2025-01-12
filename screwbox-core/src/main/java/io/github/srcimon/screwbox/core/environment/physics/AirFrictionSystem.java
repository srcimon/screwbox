package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

import static io.github.srcimon.screwbox.core.utils.MathUtil.modifier;

//TODO javadoc
//TODO changelog
//TODO test
public class AirFrictionSystem implements EntitySystem {

    private static final Archetype PHYSICS = Archetype.of(PhysicsComponent.class, AirFrictionComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(PHYSICS)) {
            final var physics = entity.get(PhysicsComponent.class);
            final var friction = entity.get(AirFrictionComponent.class);
            final double frictionX = friction.frictionX * engine.loop().delta();
            final double frictionY = friction.frictionY * engine.loop().delta();
            final double absX = Math.abs(physics.momentum.x());
            final double absY = Math.abs(physics.momentum.y());
            final double changeX = Math.clamp(modifier(physics.momentum.x()) * frictionX * -1, -absX, absX);
            final double changeY = Math.clamp(modifier(physics.momentum.y()) * frictionY * -1, -absY, absY);
            physics.momentum = physics.momentum.add(changeX, changeY);
        }
    }
}
