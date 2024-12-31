package io.github.srcimon.screwbox.physicsplayground.player.states;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.keyboard.Key;

public class PlayerStandingState implements EntityState {

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if(engine.keyboard().isPressed(Key.SPACE)) {
            return new PlayerJumpingState();
        }
        if(!entity.get(PhysicsComponent.class).momentum.isZero()) {
            return new PlayerWalkingState();
        }
        return this;
    }
}
