package io.github.srcimon.screwbox.playground.scene.player.states;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.ClimbComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.DashControlComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.GrabComponent;
import io.github.srcimon.screwbox.core.environment.controls.JumpControlComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.WallJumpComponent;

public class WalkState implements EntityState {

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(DashControlComponent.class).isEnabled = false;
        entity.get(DashControlComponent.class).remainingDashes = 1;
        entity.get(JumpControlComponent.class).isEnabled = true;
        entity.get(WallJumpComponent.class).isEnabled = false;
        entity.get(GrabComponent.class).isEnabled = true;
        entity.get(ClimbComponent.class).isEnabled = false;
        entity.get(GrabComponent.class).stamina = 2;
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if(Duration.since(entity.get(JumpControlComponent.class).lastActivation).isLessThan(Duration.ofMillis(100))) {
            return new JumpState();
        }
        if (!entity.get(CollisionDetailsComponent.class).touchesBottom) {
            return new BeforeFallState();
        }
        return this;
    }
}
