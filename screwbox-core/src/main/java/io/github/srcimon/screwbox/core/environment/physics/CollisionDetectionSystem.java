package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Bounds;
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

            final Bounds sensorBounds = sensorEntity.bounds().expand(0.001);
            for (final var collider : colliders) {
                if (sensorEntity != collider && sensorBounds.intersects(collider.bounds())) {
                    if (new CollisionCheck(sensorEntity, collider).isNoOneWayFalsePositive()) {
                        collidedEntities.add(collider);
                    }
                }
            }
        }
    }
}