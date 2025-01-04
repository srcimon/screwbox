package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.states;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.controls.JumpComponent;

public class PlayerIdleState implements EntityState {

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.replace(new JumpComponent());
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if(engine.keyboard().isAnyKeyPressed()) {
            return new StandingState();
        }
        if(entity.get(JumpComponent.class).last.isSet()) {
            return new JumpingState();
        }
        return this;
    }
}
