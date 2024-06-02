package io.github.srcimon.screwbox.vacuum.player.attack;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetectionComponent;

public class ShootUnderwayState implements EntityState {
    @Override
    public EntityState update(Entity entity, Engine engine) {
        return entity.get(CollisionDetectionComponent.class).collidedEntities.isEmpty()
                ? this
                : new ShootDissolvingState();
    }
}
