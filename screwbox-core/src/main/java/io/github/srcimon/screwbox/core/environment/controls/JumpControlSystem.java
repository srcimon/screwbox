package io.github.srcimon.screwbox.core.environment.controls;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

public class JumpControlSystem implements EntitySystem {

    private static final Archetype JUMPERS = Archetype.ofSpacial(JumpControlComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var jumper : engine.environment().fetchAll(JUMPERS)) {
            final var control = jumper.get(JumpControlComponent.class);
            if (control.isEnabled && engine.keyboard().isDown(control.key)) {
                final var physics = jumper.get(PhysicsComponent.class);
                physics.momentum = physics.momentum.replaceY(-control.acceleration);
                control.lastActivation = engine.loop().time();
            }
        }
    }
}

