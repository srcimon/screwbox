package io.github.srcimon.screwbox.playground.scene.player.states;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.playground.movement.JumpComponent;

public class WalkState implements EntityState {

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(JumpComponent.class).isEnabled = true;
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.get(CollisionDetailsComponent.class).lastBottomContact.addMillis(100).isBefore(engine.loop().time())) {
            return new FallState();
        }
        return this;
    }
}
