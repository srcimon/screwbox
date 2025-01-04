package io.github.srcimon.screwbox.playgrounds.playercontrolls.states;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;

public class PlayerIdleState implements EntityState {

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if(engine.keyboard().isAnyKeyPressed()) {
            return new StandingState();
        }
        return this;
    }
}
