package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.states;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;

public class JumpingState implements EntityState {

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.get(CollisionDetailsComponent.class).touchesBottom) {
            return new StandingState();
        }
        return this;
    }
}
