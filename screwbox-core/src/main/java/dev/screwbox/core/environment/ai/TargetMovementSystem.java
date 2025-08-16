package dev.screwbox.core.environment.ai;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

/**
 * Moves entities having {@link TargetMovementComponent} towards the specified position.
 */
public class TargetMovementSystem implements EntitySystem {

    public static final Archetype TARGETS = Archetype.ofSpacial(PhysicsComponent.class, TargetMovementComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(TARGETS)) {
            final var physics = entity.get(PhysicsComponent.class);
            final var target = entity.get(TargetMovementComponent.class);
            var destinationVector = target.position.substract(entity.position());
            double deltaAcceleration = engine.loop().delta() * target.acceleration;
            Vector speedChange = destinationVector.length(deltaAcceleration);

            if (physics.velocity.length() > destinationVector.length() * 10.0) {
                speedChange = physics.velocity.invert().length(deltaAcceleration);
            }
            final Vector updatedVelocity = physics.velocity.add(speedChange);
            physics.velocity = updatedVelocity.length(Math.min(updatedVelocity.length(), target.maxSpeed));
        }
    }
}
