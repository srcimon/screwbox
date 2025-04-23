package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.physics.internal.CollisionCheck;
import dev.screwbox.core.utils.Validate;

/**
 * Collects all collided {@link Entity entities} for any {@link Entity} having {@link CollisionSensorComponent}.
 *
 * @see CollisionSensorComponent
 * @see CollisionDetailsComponent
 */
public class CollisionSensorSystem implements EntitySystem {

    private static final Archetype SENSORS = Archetype.ofSpacial(CollisionSensorComponent.class);
    private static final Archetype COLLIDERS = Archetype.ofSpacial(ColliderComponent.class);

    @Override
    public void update(final Engine engine) {
        final var colliders = engine.environment().fetchAll(COLLIDERS);
        for (final var sensorEntity : engine.environment().fetchAll(SENSORS)) {
            final var sensor = sensorEntity.get(CollisionSensorComponent.class);
            final var collidedEntities = sensor.collidedEntities;
            collidedEntities.clear();
            Validate.positive(sensor.range, "sensor range must be positive");
            final Bounds sensorBounds = sensorEntity.bounds().expand(sensor.range);
            for (final var collider : colliders) {
                if (sensorEntity != collider && sensorBounds.intersects(collider.bounds())
                    && new CollisionCheck(sensorEntity, collider).isNoOneWayFalsePositive()) {
                    collidedEntities.add(collider);
                }
            }
        }
    }
}