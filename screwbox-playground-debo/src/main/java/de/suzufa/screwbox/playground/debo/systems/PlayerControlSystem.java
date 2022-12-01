package de.suzufa.screwbox.playground.debo.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.Order;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.keyboard.Key;
import de.suzufa.screwbox.core.keyboard.KeyCombination;
import de.suzufa.screwbox.playground.debo.components.PlayerControlComponent;

@Order(UpdatePriority.SIMULATION_BEGIN)
public class PlayerControlSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerControlComponent.class, PhysicsBodyComponent.class);
    private static final KeyCombination JUMP_DOWN = KeyCombination.ofKeys(Key.ARROW_DOWN, Key.SPACE);

    @Override
    public void update(Engine engine) {
        for (Entity entity : engine.entities().fetchAll(PLAYER)) {
            var control = entity.get(PlayerControlComponent.class);
            final var physicsBodyComponent = entity.get(PhysicsBodyComponent.class);
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
