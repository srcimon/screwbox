package io.github.srcimon.screwbox.playground.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;

public class MovementControlSystem implements EntitySystem {

    private static final Archetype MOVERS = Archetype.ofSpacial(MovementControlComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var mover : engine.environment().fetchAll(MOVERS)) {
            final var control = mover.get(MovementControlComponent.class);
            if(control.isEnabled) {
                final Keyboard keyboard = engine.keyboard();
                var speed = keyboard.movement(control.left, control.right, control.acceleration);

                var physics = mover.get(PhysicsComponent.class);
                var xSpeed = Math.clamp(physics.momentum.x() + speed * engine.loop().delta(), -control.maxSpeed, control.maxSpeed);
                physics.momentum = Vector.of(xSpeed, physics.momentum.y());
            }
        }
    }

}
