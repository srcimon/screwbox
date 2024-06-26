package io.github.srcimon.screwbox.vacuum.player.movement;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.keyboard.Key;

public class MovementControlSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(MovementControlComponent.class);

    @Override
    public void update(Engine engine) {
        engine.environment().tryFetchSingleton(PLAYER).ifPresent(player -> {
            if (engine.keyboard().isPressed(Key.SPACE)) {
                Vector wsadMovement = engine.keyboard().wsadMovement(220);
                if (!wsadMovement.isZero()) {
                    player.add(new DashComponent(wsadMovement, Duration.ofMillis(300)));
                }
            } else {
                player.get(PhysicsComponent.class).momentum = engine.keyboard().wsadMovement(80);
            }
        });
    }
}
