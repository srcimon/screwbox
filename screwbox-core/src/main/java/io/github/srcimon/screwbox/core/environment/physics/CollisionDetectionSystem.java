package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.physics.internal.CollisionCheck;

public class CollisionDetectionSystem implements EntitySystem {

    private static final Archetype SENSORS = Archetype.ofSpacial(CollisionDetectionComponent.class);
    private static final Archetype COLLIDERS = Archetype.ofSpacial(ColliderComponent.class);

    @Override
    public void update(final Engine engine) {
        final var colliders = engine.environment().fetchAll(COLLIDERS);
        for (final var sensorEntity : engine.environment().fetchAll(SENSORS)) {
            final var collidedEntities = sensorEntity.get(CollisionDetectionComponent.class).collidedEntities;
            collidedEntities.clear();

            for (final var collider : colliders) {
                if (sensorEntity != collider && sensorEntity.bounds().touches(collider.bounds())) {
                    final CollisionCheck check = new CollisionCheck(sensorEntity, collider);
                    if (check.isNoOneWayFalsePositive()) {
                        collidedEntities.add(collider);
                    }
                }
            }
        }
    }
}