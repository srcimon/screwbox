package de.suzufa.screwbox.examples.platformer.specials.player;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntityState;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.examples.platformer.components.DeathEventComponent;
import de.suzufa.screwbox.examples.platformer.components.GroundDetectorComponent;

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
