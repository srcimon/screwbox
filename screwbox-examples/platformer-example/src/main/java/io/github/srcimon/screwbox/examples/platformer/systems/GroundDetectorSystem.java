package io.github.srcimon.screwbox.examples.platformer.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ecosphere.*;
import io.github.srcimon.screwbox.core.ecosphere.components.CollisionSensorComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.TransformComponent;
import io.github.srcimon.screwbox.examples.platformer.components.GroundDetectorComponent;

@Order(SystemOrder.PREPARATION)
public class GroundDetectorSystem implements EntitySystem {

    private static final Archetype GROUND_DETECTORS = Archetype.of(GroundDetectorComponent.class,
            CollisionSensorComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.ecosphere().fetchAll(GROUND_DETECTORS)) {
            entity.get(GroundDetectorComponent.class).isOnGround = isOnGround(entity);
        }
    }

    private boolean isOnGround(final Entity entity) {
        CollisionSensorComponent sensor = entity.get(CollisionSensorComponent.class);
        if (sensor.collidedEntities.isEmpty()) {
            return false;
        }

        Bounds entityBounds = entity.get(TransformComponent.class).bounds;
        for (final Entity other : sensor.collidedEntities) {
            Bounds otherBounds = other.get(TransformComponent.class).bounds;
            if (otherBounds.minY() + 1 >= entityBounds.maxY()
                    && otherBounds.minX() <= entityBounds.maxX() - 1
                    && otherBounds.maxX() >= entityBounds.minX() + 1) {
                return true;
            }
        }
        return false;
    }
}
