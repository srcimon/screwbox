package io.github.srcimon.screwbox.playground.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

public class ClimbSystem implements EntitySystem {

    private static final Archetype CLIMBERS = Archetype.ofSpacial(ClimbComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var mover : engine.environment().fetchAll(CLIMBERS)) {
            final var control = mover.get(ClimbComponent.class);
            if (control.isEnabled) {
                final var physics = mover.get(PhysicsComponent.class);
                if (engine.keyboard().isDown(control.keyUp)) {
                    physics.momentum = Vector.$(physics.momentum.x(), -control.speed);
                } else if (engine.keyboard().isDown(control.keyDown)) {
                    physics.momentum = Vector.$(physics.momentum.x(), control.speed);
                } else {
                    physics.momentum = Vector.$(physics.momentum.x(), physics.momentum.y() / 2.0);
                }
            }
        }
    }
}

