package io.github.srcimon.screwbox.core.ecosphere.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ecosphere.Archetype;
import io.github.srcimon.screwbox.core.ecosphere.EntitySystem;
import io.github.srcimon.screwbox.core.ecosphere.components.ColliderComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.CollisionSensorComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.TransformComponent;
import io.github.srcimon.screwbox.core.physics.internal.CollisionCheck;

public class CollisionSensorSystem implements EntitySystem {

    private static final Archetype SENSORS = Archetype.of(TransformComponent.class, CollisionSensorComponent.class);
    private static final Archetype COLLIDERS = Archetype.of(TransformComponent.class, ColliderComponent.class);

    @Override
    public void update(final Engine engine) {
        final var colliders = engine.ecosphere().fetchAll(COLLIDERS);
        for (final var sensorEntity : engine.ecosphere().fetchAll(SENSORS)) {
            final var collidedEntities = sensorEntity.get(CollisionSensorComponent.class).collidedEntities;
            collidedEntities.clear();

            for (final var collider : colliders) {
                if (sensorEntity != collider) {
                    final CollisionCheck check = new CollisionCheck(sensorEntity, collider);
                    if (check.bodiesTouch() && check.isNoOneWayFalsePositive()) {
                        collidedEntities.add(collider);
                    }
                }
            }
        }
    }
}