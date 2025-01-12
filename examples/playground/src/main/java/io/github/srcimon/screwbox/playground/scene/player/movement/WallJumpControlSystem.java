package io.github.srcimon.screwbox.playground.scene.player.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;
import io.github.srcimon.screwbox.playground.scene.player.states.JumpState;

import static io.github.srcimon.screwbox.core.Vector.$;

public class WallJumpControlSystem implements EntitySystem {

    private static final Archetype JUMPERS = Archetype.ofSpacial(WallJumpComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var entity : engine.environment().fetchAll(JUMPERS)) {
            final var jumpConfig = entity.get(WallJumpComponent.class);
            if (jumpConfig.isEnabled && engine.keyboard().isPressed(jumpConfig.keyJump)) {
                entity.get(StateComponent.class).forcedState = new JumpState();
                final var physics = entity.get(PhysicsComponent.class);
                applyJump(engine.keyboard(), jumpConfig, physics);
            }
        }
    }

    private static void applyJump(final Keyboard keyboard, WallJumpComponent jumpConfig, PhysicsComponent physics) {
        if (keyboard.isDown(Key.ARROW_RIGHT) && jumpConfig.isLeft) {
            physics.momentum = $(jumpConfig.strongAcceleration, -jumpConfig.minorAcceleration);
        } else if (keyboard.isDown(Key.ARROW_LEFT) && !jumpConfig.isLeft) {
            physics.momentum = $(-jumpConfig.strongAcceleration, -jumpConfig.minorAcceleration);
        } else {
            physics.momentum = $(jumpConfig.isLeft ? jumpConfig.minorAcceleration : -jumpConfig.minorAcceleration, -jumpConfig.strongAcceleration);
        }
    }
}
