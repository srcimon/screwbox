package io.github.srcimon.screwbox.vacuum.player.attack;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetectionComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenScaleComponent;

public class ShootDissolvingState implements EntityState {

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.remove(PhysicsComponent.class);
        entity.remove(CollisionDetectionComponent.class);
        entity.add(new TweenComponent(Duration.ofMillis(200)));
        entity.add(new TweenScaleComponent(0, 1));
        entity.add(new TweenDestroyComponent());
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        return this;
    }
}
