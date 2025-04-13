package dev.screwbox.vacuum.player.death;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.logic.EntityState;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.tweening.TweenComponent;
import dev.screwbox.core.environment.tweening.TweenScaleComponent;
import dev.screwbox.vacuum.player.movement.MovementControlComponent;

public class FallToDeathState implements EntityState {

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.add(new TweenComponent(Duration.ofMillis(800)));
        entity.add(new TweenScaleComponent(0, 1));
        entity.remove(MovementControlComponent.class);
        entity.get(PhysicsComponent.class).momentum = Vector.zero();
        engine.audio().playSound(SoundBundle.STEAM);
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (!entity.hasComponent(TweenComponent.class)) {
            engine.environment().remove(entity);
            engine.scenes().resetActiveScene();
        }
        return this;
    }
}
