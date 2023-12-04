package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.components.CollisionSensorComponent;
import io.github.srcimon.screwbox.examples.platformer.components.CollectableComponent;
import io.github.srcimon.screwbox.examples.platformer.components.PlayerMarkerComponent;

import static io.github.srcimon.screwbox.core.audio.Sound.assetFromFile;

public class CollectableSystem implements EntitySystem {

    private static final Archetype COLLECTABLES = Archetype.of(CollisionSensorComponent.class,
            CollectableComponent.class);

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class);

    private static final Asset<Sound> PLING = assetFromFile("sounds/pling.wav");

    @Override
    public void update(final Engine engine) {
        final var player = engine.environment().forcedFetch(PLAYER);
        for (final Entity entity : engine.environment().fetchAll(COLLECTABLES)) {
            if (entity.get(CollisionSensorComponent.class).collidedEntities.contains(player)) {
                engine.environment().remove(entity);
                engine.audio().playEffect(PLING.get());
            }
        }

    }

}
