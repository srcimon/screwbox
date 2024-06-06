package io.github.srcimon.screwbox.vacuum.enemies;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

public class HurtSystem implements EntitySystem {

    private static final Archetype HURT_ENEMIES = Archetype.of(EnemyComponent.class, HurtComponent.class, TransformComponent.class);

    @Override
    public void update(Engine engine) {
        for(final var enemy : engine.environment().fetchAll(HURT_ENEMIES)) {
            engine.audio().playSound(SoundBundle.SPLASH);
            engine.environment().remove(enemy);
        }

    }
}
