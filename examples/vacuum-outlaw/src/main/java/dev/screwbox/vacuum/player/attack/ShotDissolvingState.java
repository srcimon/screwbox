package dev.screwbox.vacuum.player.attack;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.logic.EntityState;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.tweening.TweenComponent;
import dev.screwbox.core.environment.tweening.TweenDestroyComponent;
import dev.screwbox.core.environment.tweening.TweenLightComponent;
import dev.screwbox.core.environment.tweening.TweenScaleComponent;

public class ShotDissolvingState implements EntityState {

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.remove(PhysicsComponent.class);
        entity.remove(CollisionSensorComponent.class);
        entity.add(new TweenComponent(Duration.ofMillis(200)));
        entity.add(new TweenScaleComponent(0, 1));
        entity.add(new TweenLightComponent(Percent.zero(), Percent.max()));
        entity.add(new TweenDestroyComponent());
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        return this;
    }
}
