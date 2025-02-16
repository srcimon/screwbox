package io.github.srcimon.screwbox.playground.scene.player.states;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.ClimbComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.DashControlComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.GrabComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.JumpControlComponent;
import io.github.srcimon.screwbox.core.environment.controls.HorizontalControlComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.WallJumpComponent;

public class BeforeFallState implements EntityState {

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(DashControlComponent.class).isEnabled = true;
        entity.get(JumpControlComponent.class).isEnabled = true;
        entity.get(HorizontalControlComponent.class).isEnabled = true;
        entity.get(ClimbComponent.class).isEnabled = false;
        entity.get(WallJumpComponent.class).isEnabled = false;
        entity.get(GrabComponent.class).isEnabled = true;
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.get(CollisionDetailsComponent.class).lastBottomContact.addMillis(200).isBefore(engine.loop().time())) {
            return new FallState();
        }
        return this;
    }
}
