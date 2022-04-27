package de.suzufa.screwbox.playground.debo.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.audio.Sound;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.components.CollisionSensorComponent;
import de.suzufa.screwbox.playground.debo.components.CollectableComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;

public class CollectableSystem implements EntitySystem {

    private static final Archetype COLLECTABLES = Archetype.of(CollisionSensorComponent.class,
            CollectableComponent.class);

    private static final Archetype PLAYER = Archetype.of(PlayerMarkerComponent.class);

    private static final Sound PLING = Sound.fromFile("sounds/pling.wav");

    @Override
    public void update(final Engine engine) {
        final var player = engine.entityEngine().forcedFetchSingle(PLAYER);
        for (final Entity entity : engine.entityEngine().fetchAll(COLLECTABLES)) {
            if (entity.get(CollisionSensorComponent.class).collidedEntities.contains(player)) {
                engine.entityEngine().remove(entity);
                engine.audio().playEffect(PLING);
            }
        }

    }

}
