package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.still;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.jump.JumpComponent;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.jump.JumpingState;

public class IdleState implements EntityState {

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.addOrReplace(new JumpComponent());
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if(engine.keyboard().isAnyKeyPressed()) {
            return new StandingState();
        }
        if(entity.get(JumpComponent.class).jumpStarted.isSet()) {
            return new JumpingState();
        }
        return this;
    }
}
