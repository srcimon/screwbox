package dev.screwbox.core.environment.controls;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

/**
 * Applies movement for all {@link Entity entities} having {@link MovementControlComponent}.
 *
 * @see MovementControlComponent
 * @since 2.15.0
 */
public class MovementControlSystem implements EntitySystem {

    private static final Archetype MOVERS = Archetype.of(MovementControlComponent.class, PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var mover : engine.environment().fetchAll(MOVERS)) {
            final var control = mover.get(MovementControlComponent.class);
            if (control.isEnabled) {
                final var physics = mover.get(PhysicsComponent.class);
                final var targetXSpeed = engine.keyboard().axisSign(control.leftAlias, control.rightAlias) * control.acceleration;
                final var xSpeed = Math.clamp(physics.velocity.x() + engine.loop().delta(targetXSpeed), -control.maxSpeed, control.maxSpeed);

                if (control.isLimitedToHorizontalMovement) {
                    physics.velocity = physics.velocity.replaceX(xSpeed);
                } else {
                    final var targetYSpeed = engine.keyboard().axisSign(control.upAlias, control.downAlias) * control.acceleration;
                    final var ySpeed = Math.clamp(physics.velocity.y() + engine.loop().delta(targetYSpeed), -control.maxSpeed, control.maxSpeed);
                    physics.velocity = Vector.$(xSpeed, ySpeed);
                }
            }
        }
    }

}
