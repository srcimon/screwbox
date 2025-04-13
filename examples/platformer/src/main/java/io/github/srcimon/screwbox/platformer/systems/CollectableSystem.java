package io.github.srcimon.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import io.github.srcimon.screwbox.platformer.achievements.Collect10ItemsAchievement;
import io.github.srcimon.screwbox.platformer.components.CollectableComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;

public class CollectableSystem implements EntitySystem {

    private static final Archetype COLLECTABLES = Archetype.of(CollisionSensorComponent.class,
            CollectableComponent.class);

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class);

    @Override
    public void update(final Engine engine) {
        final var player = engine.environment().fetchSingleton(PLAYER);
        for (final Entity entity : engine.environment().fetchAll(COLLECTABLES)) {
            if (entity.get(CollisionSensorComponent.class).collidedEntities.contains(player)) {
                engine.environment().remove(entity);
                engine.audio().playSound(SoundBundle.PLING);
                engine.achievements().progress(Collect10ItemsAchievement.class);
            }
        }

    }

}
