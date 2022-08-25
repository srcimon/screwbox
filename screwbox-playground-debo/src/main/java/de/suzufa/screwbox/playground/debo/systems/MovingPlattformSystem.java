package de.suzufa.screwbox.playground.debo.systems;

import static de.suzufa.screwbox.core.utils.MathUtil.clamp;
import static java.util.Objects.nonNull;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.CollisionSensorComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.playground.debo.components.MovingPlattformComponent;
import de.suzufa.screwbox.playground.debo.components.WaypointComponent;

public class MovingPlattformSystem implements EntitySystem {

    private static final Archetype PLATTFORMS = Archetype.of(MovingPlattformComponent.class, TransformComponent.class);

    @Override
    public void update(Engine engine) {
        double delta = engine.loop().delta();
        for (var entity : engine.entityEngine().fetchAll(PLATTFORMS)) {
            var plattformComponent = entity.get(MovingPlattformComponent.class);
            var transform = entity.get(TransformComponent.class);

            if (plattformComponent.targetPosition == null) {
                Entity tartetEntity = engine.entityEngine().forcedFetchById(plattformComponent.waypoint);
                plattformComponent.targetPosition = tartetEntity.get(TransformComponent.class).bounds.position();
            }

            Vector distance = transform.bounds.position().substract(plattformComponent.targetPosition);

            if (distance.x() == 0 && distance.y() == 0) {
                Entity currentTarget = engine.entityEngine().forcedFetchById(plattformComponent.waypoint);
                Entity nextTarget = engine.entityEngine()
                        .forcedFetchById(currentTarget.get(WaypointComponent.class).next);
                plattformComponent.targetPosition = nextTarget.get(TransformComponent.class).bounds.position();
                plattformComponent.waypoint = nextTarget.id().orElseThrow();
            }
            double xSpeed = clamp(delta * -1 * plattformComponent.speed,
                    -1 * distance.x(),
                    delta * plattformComponent.speed);

            double ySpeed = clamp(delta * -1 * plattformComponent.speed,
                    -1 * distance.y(),
                    delta * plattformComponent.speed);

            Vector movement = Vector.of(xSpeed, ySpeed);

            var sensor = entity.get(CollisionSensorComponent.class);
            if (nonNull(sensor)) {
                for (final Entity triggeringEntities : sensor.collidedEntities) {
                    if (triggeringEntities.hasComponent(PhysicsBodyComponent.class)) {
                        final var colliderTransform = triggeringEntities.get(TransformComponent.class);
                        if (transform.bounds.minY() + 1 >= colliderTransform.bounds.maxY()) {
                            colliderTransform.bounds = colliderTransform.bounds.moveBy(movement);
                        }
                    }
                }
            }
            transform.bounds = transform.bounds.moveBy(movement);
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.SIMULATION_BEGIN;
    }
}
