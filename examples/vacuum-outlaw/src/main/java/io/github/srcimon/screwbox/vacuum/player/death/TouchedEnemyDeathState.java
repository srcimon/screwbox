package io.github.srcimon.screwbox.vacuum.player.death;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;

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
