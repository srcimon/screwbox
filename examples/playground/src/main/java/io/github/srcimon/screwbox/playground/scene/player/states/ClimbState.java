package io.github.srcimon.screwbox.playground.scene.player.states;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.ClimbComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.DashControlComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.JumpControlComponent;
import io.github.srcimon.screwbox.core.environment.controls.LeftRightControlComponent;
import io.github.srcimon.screwbox.playground.scene.player.movement.WallJumpComponent;

public class ClimbState implements EntityState {

    private final boolean left;

    public ClimbState(boolean left) {
        this.left = left;
    }

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(DashControlComponent.class).isEnabled = true;
        entity.get(LeftRightControlComponent.class).isEnabled = false;
        PhysicsComponent physics = entity.get(PhysicsComponent.class);
        physics.gravityModifier = 0;
        physics.momentum = Vector.zero();
        entity.get(ClimbComponent.class).isEnabled = true;
        entity.get(JumpControlComponent.class).isEnabled = false;
        entity.get(WallJumpComponent.class).isEnabled = true;
        entity.get(WallJumpComponent.class).isLeft = left;
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        entity.get(PhysicsComponent.class).momentum = Vector.x(left ? -50 : 50);
        return new FallState();
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        entity.get(PhysicsComponent.class).gravityModifier = 1;
    }
}
