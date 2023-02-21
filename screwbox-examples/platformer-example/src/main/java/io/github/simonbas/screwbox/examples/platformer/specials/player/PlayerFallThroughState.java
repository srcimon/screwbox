package io.github.simonbas.screwbox.examples.platformer.specials.player;

import io.github.simonbas.screwbox.core.Duration;
import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Time;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.EntityState;
import io.github.simonbas.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.simonbas.screwbox.examples.platformer.components.DeathEventComponent;
import io.github.simonbas.screwbox.examples.platformer.components.GroundDetectorComponent;

public class PlayerFallThroughState implements EntityState {

    private static final long serialVersionUID = 1L;

    private final Time started = Time.now();

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(PhysicsBodyComponent.class).ignoreOneWayCollisions = true;
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.hasComponent(DeathEventComponent.class)) {
            return new PlayerDeathState();
        }
        if (entity.get(GroundDetectorComponent.class).isOnGround) {
            return new PlayerStandingState();
        }
        if (Duration.since(started).isAtLeast(Duration.ofMillis(160))) {
            return new PlayerJumpingState();
        }
        return this;
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        entity.get(PhysicsBodyComponent.class).ignoreOneWayCollisions = false;
    }

}
