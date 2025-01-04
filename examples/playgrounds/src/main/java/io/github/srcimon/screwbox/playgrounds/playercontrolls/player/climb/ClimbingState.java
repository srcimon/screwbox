package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.climb;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.jump.JumpingState;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.move.MovementComponent;

public class ClimbingState implements EntityState {

    private Time started = Time.now();

    @Override
    public void enter(Entity entity, Engine engine) {
        final var physics = entity.get(PhysicsComponent.class);
        entity.remove(MovementComponent.class);
        physics.gravityModifier = 0;
        physics.momentum = Vector.zero();
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if(Duration.since(started).isAtLeast(Duration.ofSeconds(2))) {
            return new JumpingState();//TODO falling state
        }
        return this;
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        entity.get(PhysicsComponent.class).gravityModifier = 1;
    }
}
