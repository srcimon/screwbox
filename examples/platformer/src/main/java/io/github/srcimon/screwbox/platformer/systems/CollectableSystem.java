package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.CollisionSensorComponent;
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
                engine.achievements().progess(Collect10ItemsAchievement.class);
            }
        }

    }

}
