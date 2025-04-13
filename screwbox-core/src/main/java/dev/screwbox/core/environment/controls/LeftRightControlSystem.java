package dev.screwbox.core.environment.controls;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.keyboard.Keyboard;

/**
 * Applies left and right movement for all {@link Entity entities} having {@link LeftRightControlComponent}.
 *
 * @see LeftRightControlComponent
 * @since 2.15.0
 */
public class LeftRightControlSystem implements EntitySystem {

    private static final Archetype MOVERS = Archetype.of(LeftRightControlComponent.class, PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var mover : engine.environment().fetchAll(MOVERS)) {
            final var control = mover.get(LeftRightControlComponent.class);
            if (control.isEnabled) {
                final var speed = speedFromInput(control.leftAlias, control.rightAlias, engine.keyboard()) * control.acceleration;
                final var physics = mover.get(PhysicsComponent.class);
                final var xSpeed = Math.clamp(physics.momentum.x() + engine.loop().delta(speed), -control.maxSpeed, control.maxSpeed);
                physics.momentum = physics.momentum.replaceX(xSpeed);
            }
        }
    }

    public double speedFromInput(final Enum<?> left, final Enum<?> right, final Keyboard keyboard) {
        if (keyboard.isDown(left)) {
            return -1;
        }
        return keyboard.isDown(right) ? 1 : 0;
    }

}
