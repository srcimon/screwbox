package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;

import java.util.List;

import static io.github.srcimon.screwbox.core.environment.Order.SystemOrder.SIMULATION_LATE;
import static java.util.Objects.nonNull;

/**
 * Processes {@link Entity entities} and adds collision details for collision collected via {@link CollisionSensorComponent} to {@link CollisionDetailsComponent}.
 *
 * @see CollisionDetailsComponent
 * @see CollisionSensorComponent
 * @since 2.10.0
 */
@Order(SIMULATION_LATE)
public class CollisionDetailsSystem implements EntitySystem {

    private static final Archetype SENSORS = Archetype.ofSpacial(CollisionSensorComponent.class, CollisionDetailsComponent.class);
    public static final double MIN_DISTANCE = 0.001;

    @Override
    public void update(final Engine engine) {
        for (final var sensor : engine.environment().fetchAll(SENSORS)) {
            var collisions = sensor.get(CollisionSensorComponent.class).collidedEntities;
            var details = sensor.get(CollisionDetailsComponent.class);
            if (collisions.isEmpty()) {
                reset(details);
            } else {
                final var sensorBounds = sensor.bounds();
                final var sensorButton = Bounds.atOrigin(sensorBounds.bottomLeft(), sensorBounds.width(), MIN_DISTANCE);
                details.entityBottom = getSensorCollision(sensorButton, collisions);
                details.touchesBottom = nonNull(details.entityBottom);

                final var sensorTop = Bounds.atOrigin(sensorBounds.origin().addY(-MIN_DISTANCE), sensorBounds.width(), MIN_DISTANCE);
                details.entityTop = getSensorCollision(sensorTop, collisions);
                details.touchesTop = nonNull(details.entityTop);

                final var sensorRight = Bounds.atOrigin(sensorBounds.topRight(), MIN_DISTANCE, sensorBounds.height());
                details.entityRight= getSensorCollision(sensorRight, collisions);
                details.touchesRight = nonNull(details.entityRight);

                final var sensorLeft = Bounds.atOrigin(sensorBounds.origin().addX(-MIN_DISTANCE), MIN_DISTANCE, sensorBounds.height());
                details.entityLeft= getSensorCollision(sensorLeft, collisions);
                details.touchesLeft = nonNull(details.entityLeft);
            }
        }
    }

    private void reset(final CollisionDetailsComponent details) {
        details.touchesBottom = false;
        details.touchesLeft = false;
        details.touchesRight = false;
        details.touchesTop = false;
        details.entityBottom = null;
        details.entityLeft = null;
        details.entityRight = null;
        details.entityTop = null;
    }

    private Entity getSensorCollision(final Bounds sensor, final List<Entity> collisions) {
        for (final var collision : collisions) {
            if (sensor.intersects(collision.bounds())) {
                return collision;
            }
        }
        return null;
    }
}
