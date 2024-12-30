package io.github.srcimon.screwbox.physicsplayground.player;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

public class PlayerControlSystem implements EntitySystem {
    @Override
    public void update(Engine engine) {
        var player = engine.environment().fetchSingleton(PlayerControlComponent.class);
        player.get(PhysicsComponent.class).momentum = engine.keyboard().wsadMovement(40);
    }
}
