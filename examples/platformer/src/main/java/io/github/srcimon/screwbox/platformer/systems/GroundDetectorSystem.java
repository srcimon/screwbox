package io.github.srcimon.screwbox.platformer.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.physics.CollisionSensorComponent;
import io.github.srcimon.screwbox.platformer.components.GroundDetectorComponent;

@Order(Order.SystemOrder.PREPARATION)
public class GroundDetectorSystem implements EntitySystem {

    private static final Archetype GROUND_DETECTORS = Archetype.of(GroundDetectorComponent.class,
            CollisionSensorComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.environment().fetchAll(GROUND_DETECTORS)) {
            entity.get(GroundDetectorComponent.class).isOnGround = isOnGround(entity);
        }
    }

    private boolean isOnGround(final Entity entity) {
        CollisionSensorComponent sensor = entity.get(CollisionSensorComponent.class);
        if (sensor.collidedEntities.isEmpty()) {
            return false;
        }

        Bounds entityBounds = entity.bounds();
        for (final Entity other : sensor.collidedEntities) {
            Bounds otherBounds = other.bounds();
            if (otherBounds.minY() + 1 >= entityBounds.maxY()
                    && otherBounds.minX() <= entityBounds.maxX() - 1
                    && otherBounds.maxX() >= entityBounds.minX() + 1) {
                return true;
            }
        }
        return false;
    }
}
