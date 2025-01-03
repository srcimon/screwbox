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

//TODO javadoc
//TODO since

@Order(SIMULATION_LATE)
public class CollisionDetailsSystem implements EntitySystem {

    private static final Archetype SENSORS = Archetype.ofSpacial(CollisionSensorComponent.class, CollisionDetailsComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var sensor : engine.environment().fetchAll(SENSORS)) {
            var collisions = sensor.get(CollisionSensorComponent.class).collidedEntities;
            var details = sensor.get(CollisionDetailsComponent.class);
            if (collisions.isEmpty()) {
                details.touchesBottom = false;
                details.touchesLeft = false;
                details.touchesRight = false;
                details.touchesTop = false;
                details.entityBottom = null;
                details.entityLeft = null;
                details.entityRight = null;
                details.entityTop = null;
                //TODO test
            } else {
                final var sensorBounds = sensor.bounds();
                final var sensorButton = Bounds.atOrigin(sensorBounds.bottomLeft(), sensorBounds.width(), 0.1);
                details.entityBottom = getSensorCollision(sensorButton, collisions);
                details.touchesBottom = nonNull(details.entityBottom);
                //TODO other sides
            }
        }
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
