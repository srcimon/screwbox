package io.github.srcimon.screwbox.playground.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;

public class MovementControlSystem implements EntitySystem {

    private static final Archetype MOVERS = Archetype.ofSpacial(MovementControlComponent.class, AccelerationComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var mover : engine.environment().fetchAll(MOVERS)) {
            final var control = mover.get(MovementControlComponent.class);
            final Keyboard keyboard = engine.keyboard();
            var speed = keyboard.movement(control.left, control.right, control.acceleration);

            var acceleration = mover.get(AccelerationComponent.class);
            acceleration.acceleration = Vector.x(speed);
            acceleration.maxSpeed = control.maxSpeed;
        }
    }

}
