package io.github.srcimon.screwbox.playgrounds.playercontrolls.states;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.keyboard.Key;

public class StandingState implements EntityState {

    private Time started = Time.now();

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (engine.keyboard().isPressed(Key.SPACE)) {
            return new JumpingState();
        }
        if(Duration.since(started).isAtLeast(Duration.ofSeconds(5))) {
            return new PlayerIdleState();
        }
        return this;
    }
}
