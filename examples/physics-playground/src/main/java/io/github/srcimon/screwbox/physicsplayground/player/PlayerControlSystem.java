package io.github.srcimon.screwbox.physicsplayground.player;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.keyboard.Key;

public class PlayerControlSystem implements EntitySystem {

    private static final Archetype PLAYERS = Archetype.of(PlayerControlComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var player : engine.environment().fetchAll(PLAYERS)) {
            var physicsComponent = player.get(PhysicsComponent.class);
            double xPower = engine.keyboard().wsadMovement(500).x();
            var newX = Math.clamp(physicsComponent.momentum.x() + xPower * engine.loop().delta(), -100, 100);
            physicsComponent.momentum = Vector.of(newX, physicsComponent.momentum.y());
            if (engine.keyboard().isPressed(Key.SPACE)) {
                engine.audio().playSound(Sound.fromFile("jump.wav"));
                physicsComponent.momentum = physicsComponent.momentum.addY(-200);
            }
        }
    }
}
