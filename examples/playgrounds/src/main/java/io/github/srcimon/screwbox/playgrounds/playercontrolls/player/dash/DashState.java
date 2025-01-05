package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.dash;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.jump.JumpingState;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.move.MovementComponent;

public class DashState implements EntityState {

    private Time started = Time.now();

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.remove(MovementComponent.class);
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if(Duration.between(engine.loop().time(), started).isAtLeast(Duration.ofMillis(250))) {
            return new JumpingState();
        }
        return this;
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        entity.add(new MovementComponent());
    }
}
