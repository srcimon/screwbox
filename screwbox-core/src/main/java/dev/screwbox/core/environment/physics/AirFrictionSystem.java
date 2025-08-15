package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;

import static dev.screwbox.core.utils.MathUtil.modifier;

/**
 * Reduces {@link PhysicsComponent#momentum} of {@link Entity entities} containing {@link AirFrictionComponent} over time.
 *
 * @see AirFrictionComponent
 * @since 2.11.0
 */
public class AirFrictionSystem implements EntitySystem {

    private static final Archetype PHYSICS = Archetype.of(PhysicsComponent.class, AirFrictionComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(PHYSICS)) {
            final var physics = entity.get(PhysicsComponent.class);
            final var friction = entity.get(AirFrictionComponent.class);
            final double frictionX = friction.frictionX * engine.loop().delta();
            final double frictionY = friction.frictionY * engine.loop().delta();
            //TODO move into new Vector method;
            final double absX = Math.abs(physics.momentum.x());
            final double absY = Math.abs(physics.momentum.y());
            final double changeX = Math.clamp(modifier(physics.momentum.x()) * frictionX * -1, -absX, absX);
            final double changeY = Math.clamp(modifier(physics.momentum.y()) * frictionY * -1, -absY, absY);
            physics.momentum = physics.momentum.add(changeX, changeY);
        }
    }
}
