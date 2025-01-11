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
                if (engine.keyboard().isDown(control.keyUp)) {
                    final var phyiscs = mover.get(PhysicsComponent.class);
                    phyiscs.momentum = Vector.y(-control.speed);
                }
                if (engine.keyboard().isDown(control.keyDown)) {
                    final var phyiscs = mover.get(PhysicsComponent.class);
                    phyiscs.momentum = Vector.y(control.speed);
                }
            }
        }
    }
}

