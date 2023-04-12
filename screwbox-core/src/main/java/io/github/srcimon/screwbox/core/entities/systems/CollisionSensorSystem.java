package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.components.ColliderComponent;
import io.github.srcimon.screwbox.core.entities.components.CollisionSensorComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.physics.internal.CollisionCheck;

public class CollisionSensorSystem implements EntitySystem {

    private static final Archetype SENSORS = Archetype.of(TransformComponent.class, CollisionSensorComponent.class);
    private static final Archetype COLLIDERS = Archetype.of(TransformComponent.class, ColliderComponent.class);

    @Override
    public void update(final Engine engine) {
        final var colliders = engine.entities().fetchAll(COLLIDERS);
        for (final var sensorEntity : engine.entities().fetchAll(SENSORS)) {
            final var collidedEntities = sensorEntity.get(CollisionSensorComponent.class).collidedEntities;
            collidedEntities.clear();

            for (final var collider : colliders) {
                final CollisionCheck check = new CollisionCheck(sensorEntity, collider);
                if (check.bodiesTouch() && check.isNoSelfCollision() && check.isNoOneWayFalsePositive()) {
                    collidedEntities.add(collider);
                }

            }
        }
    }
}