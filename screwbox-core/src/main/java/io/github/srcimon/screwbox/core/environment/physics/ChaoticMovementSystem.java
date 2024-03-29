package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

/**
 * Enables chaotic movement behaviour for all {@link Entity}s having {@link PhysicsComponent} and {@link ChaoticMovementComponent}.
 */
public class ChaoticMovementSystem implements EntitySystem {

    private static final Archetype MOVING_ENTITIES = Archetype.of(PhysicsComponent.class, ChaoticMovementComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(MOVING_ENTITIES)) {
            final var movement = entity.get(ChaoticMovementComponent.class);
            final Vector chaoticMovement = Vector.of(
                            movement.xModifier.value(engine.loop().lastUpdate()),
                            movement.yModifier.value(engine.loop().lastUpdate()))
                    .multiply(movement.speed);
            entity.get(PhysicsComponent.class).momentum = chaoticMovement;
        }
    }
}
