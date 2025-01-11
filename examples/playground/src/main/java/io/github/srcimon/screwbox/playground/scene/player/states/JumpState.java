package io.github.srcimon.screwbox.playground.scene.player.states;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.playground.movement.JumpControlComponent;
import io.github.srcimon.screwbox.playground.movement.WallJumpComponent;

public class JumpState implements EntityState {

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(JumpControlComponent.class).isEnabled = false;
        entity.get(WallJumpComponent.class).isEnabled = false;
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        return new FallState();
    }
}
