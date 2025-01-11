package io.github.srcimon.screwbox.playground.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

import static java.util.Objects.nonNull;

public class JumpSystem implements EntitySystem {

    private static final Archetype JUMPERS = Archetype.ofSpacial(JumpComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var mover : engine.environment().fetchAll(JUMPERS)) {
            final var control = mover.get(JumpComponent.class);
            if (control.isEnabled) {
                if (engine.keyboard().isDown(control.key)) {
                    final var phyiscs = mover.get(PhysicsComponent.class);
                    phyiscs.momentum = Vector.of(phyiscs.momentum.x(), -control.acceleration);
                    if (nonNull(control.jumpState)) {
                        mover.get(StateComponent.class).forcedState = control.jumpState;
                    }
                }
            }
        }
    }
}

