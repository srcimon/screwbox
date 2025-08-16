package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;

import static dev.screwbox.core.Vector.$;

/**
 * Enables chaotic movement behaviour for all {@link Entity}s having {@link PhysicsComponent} and {@link ChaoticMovementComponent}.
 */
public class ChaoticMovementSystem implements EntitySystem {

    private static final Archetype MOVING_ENTITIES = Archetype.of(PhysicsComponent.class, ChaoticMovementComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(MOVING_ENTITIES)) {
            final var movement = entity.get(ChaoticMovementComponent.class);
            final Vector targetMovement = $(
                    movement.xModifier.value(engine.loop().time()),
                    movement.yModifier.value(engine.loop().time()))
                    .multiply(movement.speed).add(movement.baseSpeed);
            final var physicsComponent = entity.get(PhysicsComponent.class);
            final Vector deltaMovement = targetMovement.substract(physicsComponent.velocity)
                    .multiply(engine.loop().delta() * 1000.0 / movement.interval.milliseconds());

            physicsComponent.velocity =  physicsComponent.velocity.add(deltaMovement);

        }
    }
}
