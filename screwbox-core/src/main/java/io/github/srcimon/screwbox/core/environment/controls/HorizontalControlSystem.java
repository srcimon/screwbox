package io.github.srcimon.screwbox.core.environment.controls;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;

public class HorizontalControlSystem implements EntitySystem {

    private static final Archetype MOVERS = Archetype.ofSpacial(HorizontalControlComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var mover : engine.environment().fetchAll(MOVERS)) {
            final var control = mover.get(HorizontalControlComponent.class);
            if (control.isEnabled) {
                var speed = movement(control.left, control.right, engine.keyboard()) * control.acceleration;

                var physics = mover.get(PhysicsComponent.class);
                var xSpeed = Math.clamp(physics.momentum.x() + speed * engine.loop().delta(), -control.maxSpeed, control.maxSpeed);
                physics.momentum = physics.momentum.replaceX(xSpeed);
            }
        }
    }

    public double movement(final Enum<?> down, final Enum<?> up, final Keyboard keyboard) {
        if (keyboard.isDown(down)) {
            return -1;
        }
        if (keyboard.isDown(up)) {
            return 1;
        }
        return 0;
    }

}
