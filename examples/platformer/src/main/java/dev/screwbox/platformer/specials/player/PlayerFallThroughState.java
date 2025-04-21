package dev.screwbox.platformer.specials.player;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.logic.EntityState;
import dev.screwbox.core.environment.physics.CollisionDetailsComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.platformer.components.DeathEventComponent;

import java.io.Serial;

public class PlayerFallThroughState implements EntityState {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Time started = Time.now();

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(PhysicsComponent.class).ignoreOneWayCollisions = true;
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.hasComponent(DeathEventComponent.class)) {
            return new PlayerDeathState();
        }
        if (entity.get(CollisionDetailsComponent.class).touchesBottom) {
            return new PlayerStandingState();
        }
        if (Duration.since(started).isAtLeast(Duration.ofMillis(160))) {
            return new PlayerJumpingState();
        }
        return this;
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        entity.get(PhysicsComponent.class).ignoreOneWayCollisions = false;
    }

}
