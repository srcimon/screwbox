package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.SoundsBundle;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetectionComponent;
import io.github.srcimon.screwbox.examples.platformer.components.CollectableComponent;
import io.github.srcimon.screwbox.examples.platformer.components.PlayerMarkerComponent;

public class CollectableSystem implements EntitySystem {

    private static final Archetype COLLECTABLES = Archetype.of(CollisionDetectionComponent.class,
            CollectableComponent.class);

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class);

    @Override
    public void update(final Engine engine) {
        final var player = engine.environment().fetchSingleton(PLAYER);
        for (final Entity entity : engine.environment().fetchAll(COLLECTABLES)) {
            if (entity.get(CollisionDetectionComponent.class).collidedEntities.contains(player)) {
                engine.environment().remove(entity);
                engine.audio().playSound(SoundsBundle.PLING);
            }
        }

    }

}
