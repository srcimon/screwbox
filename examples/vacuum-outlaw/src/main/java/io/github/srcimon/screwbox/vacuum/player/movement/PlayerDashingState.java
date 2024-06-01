package io.github.srcimon.screwbox.vacuum.player.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;

public class PlayerDashingState implements EntityState {

    @Override
    public EntityState update(Entity entity, Engine engine) {
        return entity.hasComponent(DashComponent.class)
                ? this
                : new PlayerWalkingState();
    }

}
