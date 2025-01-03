package io.github.srcimon.screwbox.playgrounds.playercontrolls;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;

public class StandingState implements EntityState {

    @Override
    public EntityState update(Entity entity, Engine engine) {
        return this;
    }
}
