package io.github.srcimon.screwbox.playground.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;

import static java.util.Objects.nonNull;

public class JumpSystem implements EntitySystem {

    private static final Archetype JUMPERS = Archetype.ofSpacial(JumpComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var mover : engine.environment().fetchAll(JUMPERS)) {
            final var control = mover.get(JumpComponent.class);
            if (control.isEnabled) {
                final Keyboard keyboard = engine.keyboard();
                if (keyboard.isPressed(control.key)) {
                    final var phyiscs = mover.get(PhysicsComponent.class);
                    phyiscs.momentum = phyiscs.momentum.addY(-control.acceleration);
                    if (nonNull(control.jumpState)) {
                        mover.get(StateComponent.class).forcedState = control.jumpState;
                    }
                }
            }
        }
    }
}

