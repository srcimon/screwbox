package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.*;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.keyboard.Key;
import io.github.srcimon.screwbox.core.keyboard.KeyCombination;
import io.github.srcimon.screwbox.platformer.components.PlayerControlComponent;

@Order(Order.SystemOrder.SIMULATION_EARLY)
public class PlayerControlSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerControlComponent.class, PhysicsComponent.class);
    private static final KeyCombination JUMP_DOWN = KeyCombination.ofKeys(Key.ARROW_DOWN, Key.SPACE);

    @Override
    public void update(Engine engine) {
        for (Entity entity : engine.environment().fetchAll(PLAYER)) {
            var control = entity.get(PlayerControlComponent.class);
            final var physicsBodyComponent = entity.get(PhysicsComponent.class);
            control.jumpDownPressed = engine.keyboard().isDown(JUMP_DOWN);
            control.jumpPressed = engine.keyboard().isDown(Key.SPACE);
            control.digPressed = engine.keyboard().isDown(Key.ARROW_DOWN);
            control.leftPressed = engine.keyboard().isDown(Key.ARROW_LEFT);
            control.rightPressed = engine.keyboard().isDown(Key.ARROW_RIGHT);

            if (control.leftPressed && physicsBodyComponent.momentum.x() > -100) {
                physicsBodyComponent.momentum = physicsBodyComponent.momentum
                        .addX(-800 * engine.loop().delta());
            } else if (control.rightPressed && physicsBodyComponent.momentum.x() < 100) {
                physicsBodyComponent.momentum = physicsBodyComponent.momentum
                        .addX(800 * engine.loop().delta());
            }

            if (control.jumpPressed && control.allowJumpPush) {
                physicsBodyComponent.momentum = physicsBodyComponent.momentum
                        .addY(-330 * engine.loop().delta());
            }

        }
    }
}
