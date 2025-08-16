package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;

/**
 * Reduces {@link PhysicsComponent#velocity} of {@link Entity entities} containing {@link FrictionComponent} over time.
 * Is independent of friction applied when colliding with collider.
 *
 * @see FrictionComponent
 * @since 2.11.0
 */
public class FrictionSystem implements EntitySystem {

    private static final Archetype PHYSICS = Archetype.of(PhysicsComponent.class, FrictionComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(PHYSICS)) {
            final var physics = entity.get(PhysicsComponent.class);
            final var friction = entity.get(FrictionComponent.class).friction;
            physics.velocity = physics.velocity.reduce(friction * engine.loop().delta());
        }
    }
}
