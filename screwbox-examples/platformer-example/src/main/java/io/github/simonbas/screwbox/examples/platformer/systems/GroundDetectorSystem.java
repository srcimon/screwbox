package io.github.simonbas.screwbox.examples.platformer.systems;

import io.github.simonbas.screwbox.core.Bounds;
import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.*;
import io.github.simonbas.screwbox.core.entities.components.CollisionSensorComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.examples.platformer.components.GroundDetectorComponent;

@Order(SystemOrder.PREPARATION)
public class GroundDetectorSystem implements EntitySystem {

    private static final Archetype GROUND_DETECTORS = Archetype.of(GroundDetectorComponent.class,
            CollisionSensorComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.entities().fetchAll(GROUND_DETECTORS)) {
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
