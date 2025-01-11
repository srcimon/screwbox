package io.github.srcimon.screwbox.playground.scene.player.states;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.playground.movement.ClimbComponent;
import io.github.srcimon.screwbox.playground.movement.GrabComponent;
import io.github.srcimon.screwbox.playground.movement.JumpControlComponent;
import io.github.srcimon.screwbox.playground.movement.WallJumpComponent;

public class WalkState implements EntityState {

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(JumpControlComponent.class).isEnabled = true;
        entity.get(WallJumpComponent.class).isEnabled = false;
        entity.get(GrabComponent.class).isEnabled = false;
        entity.get(ClimbComponent.class).isEnabled = false;
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.get(CollisionDetailsComponent.class).lastBottomContact.addMillis(100).isBefore(engine.loop().time())) {
            return new FallState();
        }
        return this;
    }
}
