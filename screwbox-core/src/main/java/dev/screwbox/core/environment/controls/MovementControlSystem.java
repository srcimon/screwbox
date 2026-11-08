package dev.screwbox.core.environment.controls;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
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
                final var physics = mover.get(PhysicsComponent.class);
                final var xSpeedSource = speedFromInput(control.leftAlias, control.rightAlias, engine.keyboard()) * control.acceleration;
                final var xSpeed = Math.clamp(physics.velocity.x() + engine.loop().delta(xSpeedSource), -control.maxSpeed, control.maxSpeed);

                if (control.isLimitedToHorizontalMovement) {
                    physics.velocity = physics.velocity.replaceX(xSpeed);
                } else {
                    final var ySpeedSource = speedFromInput(control.upAlias, control.downAlias, engine.keyboard()) * control.acceleration;
                    final var ySpeed = Math.clamp(physics.velocity.y() + engine.loop().delta(ySpeedSource), -control.maxSpeed, control.maxSpeed);
                    physics.velocity = Vector.$(xSpeed, ySpeed);
                }
            }
        }
    }

    public static double speedFromInput(final Enum<?> low, final Enum<?> high, final Keyboard keyboard) {
        if (keyboard.isDown(low)) {
            return -1;
        }
        return keyboard.isDown(high) ? 1 : 0;
    }
}
