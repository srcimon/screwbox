package de.suzufa.screwbox.core.entities.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.components.ColliderComponent;
import de.suzufa.screwbox.core.entities.components.CollisionSensorComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.physics.internal.CollisionPair;

public class CollisionSensorSystem implements EntitySystem {

    private static final Archetype SENSORS = Archetype.of(TransformComponent.class, CollisionSensorComponent.class);
    private static final Archetype COLLIDERS = Archetype.of(TransformComponent.class, ColliderComponent.class);

    @Override
    public void update(final Engine engine) {
        final var colliders = engine.entityEngine().fetchAll(COLLIDERS);
        for (final var sensorEntity : engine.entityEngine().fetchAll(SENSORS)) {
            final var collidedEntities = sensorEntity.get(CollisionSensorComponent.class).collidedEntities;
            collidedEntities.clear();

            for (final var collider : colliders) {
                final CollisionPair check = new CollisionPair(sensorEntity, collider);
                if (check.bodiesTouch() && check.isNoSelfCollision() && check.isNoOneWayFalsePositive()) {
                    collidedEntities.add(collider);
                }

            }
        }
    }
}