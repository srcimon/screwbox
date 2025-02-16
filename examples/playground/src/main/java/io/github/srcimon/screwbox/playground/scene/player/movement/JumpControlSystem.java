package io.github.srcimon.screwbox.playground.scene.player.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

import static java.util.Objects.nonNull;

public class JumpControlSystem implements EntitySystem {

    private static final Archetype JUMPERS = Archetype.ofSpacial(JumpControlComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var jumper : engine.environment().fetchAll(JUMPERS)) {
            final var control = jumper.get(JumpControlComponent.class);
            if (control.isEnabled && engine.keyboard().isDown(control.key)) {
                final var physics = jumper.get(PhysicsComponent.class);
                physics.momentum = physics.momentum.replaceY(-control.acceleration);
                if (nonNull(control.jumpState)) {
                    jumper.get(StateComponent.class).forcedState = control.jumpState;
                }
            }
        }
    }
}

