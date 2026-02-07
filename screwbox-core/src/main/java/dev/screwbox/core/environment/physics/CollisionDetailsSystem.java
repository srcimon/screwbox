package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;

import java.util.List;

import static dev.screwbox.core.environment.Order.SIMULATION_LATE;
import static java.util.Objects.nonNull;

/**
 * Processes {@link Entity entities} and adds collision details for collision collected via {@link CollisionSensorComponent} to {@link CollisionDetailsComponent}.
 *
 * @see CollisionDetailsComponent
 * @see CollisionSensorComponent
 * @since 2.10.0
 */
@ExecutionOrder(SIMULATION_LATE)
public class CollisionDetailsSystem implements EntitySystem {

    private static final Archetype SENSORS = Archetype.ofSpacial(CollisionSensorComponent.class, CollisionDetailsComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var sensorEntity : engine.environment().fetchAll(SENSORS)) {
            final var sensor = sensorEntity.get(CollisionSensorComponent.class);
            final var collisions = sensor.collidedEntities;
            final var details = sensorEntity.get(CollisionDetailsComponent.class);
            if (collisions.isEmpty()) {
                reset(details);
            } else {
                final var sensorBounds = sensorEntity.bounds();
                final var sensorButton = Bounds.atOrigin(sensorBounds.bottomLeft(), sensorBounds.width(), sensor.range);
                details.entityBottom = getSensorCollision(sensorButton, collisions);
                details.touchesBottom = nonNull(details.entityBottom);
                if (details.touchesBottom) {
                    details.lastBottomContact = engine.loop().time();
                }

                final var sensorTop = Bounds.atOrigin(sensorBounds.origin().addY(-sensor.range), sensorBounds.width(), sensor.range);
                details.entityTop = getSensorCollision(sensorTop, collisions);
                details.touchesTop = nonNull(details.entityTop);

                final var sensorRight = Bounds.atOrigin(sensorBounds.topRight(), sensor.range, sensorBounds.height());
                details.entityRight = getSensorCollision(sensorRight, collisions);
                details.touchesRight = nonNull(details.entityRight);

                final var sensorLeft = Bounds.atOrigin(sensorBounds.origin().addX(-sensor.range), sensor.range, sensorBounds.height());
                details.entityLeft = getSensorCollision(sensorLeft, collisions);
                details.touchesLeft = nonNull(details.entityLeft);
            }
        }
    }

    private static void reset(final CollisionDetailsComponent details) {
        details.touchesBottom = false;
        details.touchesLeft = false;
        details.touchesRight = false;
        details.touchesTop = false;
        details.entityBottom = null;
        details.entityLeft = null;
        details.entityRight = null;
        details.entityTop = null;
    }

    private static Entity getSensorCollision(final Bounds sensor, final List<Entity> collisions) {
        for (final var collision : collisions) {
            if (sensor.intersects(collision.bounds())) {
                return collision;
            }
        }
        return null;
    }
}
