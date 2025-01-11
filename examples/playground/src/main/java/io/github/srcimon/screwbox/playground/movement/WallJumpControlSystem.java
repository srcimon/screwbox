package io.github.srcimon.screwbox.playground.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.playground.scene.player.states.JumpState;

public class WallJumpControlSystem implements EntitySystem {

    private static final Archetype JUMPERS = Archetype.ofSpacial(WallJumpComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var entity : engine.environment().fetchAll(JUMPERS)) {
            final var jumpConfig = entity.get(WallJumpComponent.class);
            if (jumpConfig.isEnabled) {
                if (engine.keyboard().isPressed(jumpConfig.key)) {
                    entity.get(StateComponent.class).forcedState = new JumpState();
                    entity.get(PhysicsComponent.class).momentum =
                            Vector.of(jumpConfig.isLeft ? jumpConfig.accelerationX : -jumpConfig.accelerationX, -jumpConfig.accelerationY);
                }
            }
        }
    }
}
