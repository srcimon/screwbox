package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.jump;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.climb.ClimbComponent;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.climb.ClimbingState;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.move.MovementComponent;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.still.StandingState;

public class JumpingState implements EntityState {

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.addIfNotPresent(new ClimbComponent());
        entity.addOrReplace(new MovementComponent());
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        final var collisionDetails = entity.get(CollisionDetailsComponent.class);
        if (collisionDetails.touchesBottom) {
            return new StandingState();
        }
        if (entity.get(ClimbComponent.class).isGrabbed) {
            return new ClimbingState();
        }
        return this;
    }
}
