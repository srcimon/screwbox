package de.suzufa.screwbox.playground.debo.systems;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.CollisionSensorComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.playground.debo.components.GroundDetectorComponent;

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

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PREPARATION;
    }
}
