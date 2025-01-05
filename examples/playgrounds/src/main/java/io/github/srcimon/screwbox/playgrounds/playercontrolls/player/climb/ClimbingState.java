package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.climb;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.dash.DashComponent;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.dash.DashState;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.dash.DashingComponent;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.jump.JumpingState;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.move.MovementComponent;

public class ClimbingState implements EntityState {

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.remove(DashComponent.class);
        entity.addIfNotPresent(new WallJumpComponent());
        entity.addIfNotPresent(new DashComponent());
        final var physics = entity.get(PhysicsComponent.class);
        entity.remove(MovementComponent.class);
        physics.gravityModifier = 0;
        physics.momentum = Vector.zero();
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        final var climb = entity.get(ClimbComponent.class);
        var collisionDetails = entity.get(CollisionDetailsComponent.class);
        if(entity.hasComponent(DashingComponent.class)) {
            return new DashState();
        }
        if (!climb.isGrabbed || (!collisionDetails.touchesLeft && !collisionDetails.touchesRight)) {
            return new JumpingState();//TODO falling state
        }
        return this;
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        entity.get(PhysicsComponent.class).gravityModifier = 1;
        entity.remove(WallJumpComponent.class);
    }
}
