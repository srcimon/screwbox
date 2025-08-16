package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;

/**
 * Reduces {@link PhysicsComponent#velocity} of {@link Entity entities} containing {@link AirFrictionComponent} over time.
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
            final var friction = entity.get(AirFrictionComponent.class).friction;
            physics.velocity = physics.velocity.reduce(friction * engine.loop().delta());
        }
    }
}
