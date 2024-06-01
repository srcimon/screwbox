package io.github.srcimon.screwbox.vacuum.player;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;

public class PlayerDashingState implements EntityState {

    @Override
    public void enter(Entity entity, Engine engine) {

    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        DashingComponent dashing = entity.get(DashingComponent.class);
        final var dashingProgress = dashing.duration.progress(dashing.started, engine.loop().startTime());
        return dashingProgress.isMax() ? new PlayerWalkingState() : this;
    }

}
