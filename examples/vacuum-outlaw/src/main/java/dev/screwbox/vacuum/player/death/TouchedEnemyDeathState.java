package dev.screwbox.vacuum.player.death;

import dev.screwbox.core.Engine;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.logic.EntityState;
import dev.screwbox.core.environment.tweening.TweenComponent;

public class TouchedEnemyDeathState implements EntityState {

    @Override
    public void enter(Entity entity, Engine engine) {
        engine.audio().playSound(SoundBundle.SPLASH);
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
