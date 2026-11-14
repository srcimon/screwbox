package dev.screwbox.core.environment.ai;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

import static java.util.Objects.nonNull;

/**
 * Moves entities having {@link TargetMovementComponent} towards the specified position.
 */
public class TargetMovementSystem implements EntitySystem {

    public static final Archetype TARGETS = Archetype.ofSpacial(PhysicsComponent.class, TargetMovementComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(TARGETS)) {
            final var physics = entity.get(PhysicsComponent.class);
            final var movement = entity.get(TargetMovementComponent.class);
            if (nonNull(movement.targetId)) {
                movement.position = engine.environment().fetchById(movement.targetId).position();
            }
            final var destinationVector = movement.position.subtract(entity.position());
            final double deltaAcceleration = engine.loop().delta() * movement.acceleration;
            physics.velocity = physics.velocity.advance(destinationVector.length(movement.maxSpeed), deltaAcceleration);
            if (physics.velocity.length() > destinationVector.length() * movement.overshootSpeedModifier) {
                physics.velocity = physics.velocity.multiply(1.0 - 1.0 / Math.max(movement.overshootSpeedModifier, 2.0));
            }
        }
    }
}
