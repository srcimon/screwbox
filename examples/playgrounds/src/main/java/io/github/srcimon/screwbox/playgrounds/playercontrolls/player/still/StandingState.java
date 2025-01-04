package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.still;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.jump.JumpComponent;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.jump.JumpingState;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.move.MovementComponent;

public class StandingState implements EntityState {

    private Time started = Time.now();

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.addOrReplace(new JumpComponent());
        entity.addOrReplace(new MovementComponent(150));
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (Duration.since(started).isAtLeast(Duration.ofSeconds(5))) {
            return new IdleState();
        }
        if(entity.get(JumpComponent.class).jumpStarted.isSet()) {
            return new JumpingState();
        }
        return this;
    }
}
