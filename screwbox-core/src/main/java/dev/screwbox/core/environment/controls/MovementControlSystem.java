package dev.screwbox.core.environment.controls;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.keyboard.Keyboard;

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
                final var speed = speedFromInput(control.leftAlias, control.rightAlias, engine.keyboard()) * control.acceleration;
                final var physics = mover.get(PhysicsComponent.class);
                final var xSpeed = Math.clamp(physics.velocity.x() + engine.loop().delta(speed), -control.maxSpeed, control.maxSpeed);
                physics.velocity = physics.velocity.replaceX(xSpeed);
            }
        }
    }

    public static double speedFromInput(final Enum<?> left, final Enum<?> right, final Keyboard keyboard) {
        if (keyboard.isDown(left)) {
            return -1;
        }
        return keyboard.isDown(right) ? 1 : 0;
    }

}
