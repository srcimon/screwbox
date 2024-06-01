package io.github.srcimon.screwbox.vacuum.player.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;

public class PlayerWalkingState implements EntityState {

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.add(new MovementControlComponent());
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        return entity.hasComponent(DashComponent.class)
                ? new PlayerDashingState()
                : this;
    }
}
