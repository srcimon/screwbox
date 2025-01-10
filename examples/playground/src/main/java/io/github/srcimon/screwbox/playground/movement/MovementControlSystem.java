package io.github.srcimon.screwbox.playground.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;

public class MovementControlSystem implements EntitySystem {

    private static final Archetype MOVERS = Archetype.ofSpacial(MovementControlComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var mover : engine.environment().fetchAll(MOVERS)) {
            final var control = mover.get(MovementControlComponent.class);
            var speed = getSpeed(engine.keyboard(), control);

            var acceleration = mover.get(AccelerationComponent.class);
            if (acceleration == null) {
                acceleration = new AccelerationComponent(control.maxSpeed);
                mover.add(acceleration);
            }
            acceleration.maxSpeed = control.maxSpeed;
            acceleration.acceleration = Vector.x(speed);
        }
    }

    private double getSpeed(final Keyboard keyboard, final MovementControlComponent control) {
        if (keyboard.isDown(control.left)) {
            return -control.acceleration;
        }

        if (keyboard.isDown(control.right)) {
            return control.acceleration;
        }
        return 0;
    }
}
