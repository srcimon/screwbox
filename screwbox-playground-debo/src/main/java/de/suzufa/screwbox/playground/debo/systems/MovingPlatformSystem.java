package de.suzufa.screwbox.playground.debo.systems;

import static de.suzufa.screwbox.core.utils.MathUtil.clamp;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.Order;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.CollisionSensorComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.playground.debo.components.MovingPlatformComponent;
import de.suzufa.screwbox.playground.debo.components.WaypointComponent;

@Order(UpdatePriority.SIMULATION_BEGIN)
public class MovingPlatformSystem implements EntitySystem {

    private static final Archetype PLATFORMS = Archetype.of(MovingPlatformComponent.class, TransformComponent.class);

    @Override
    public void update(Engine engine) {
        for (var platform : engine.entities().fetchAll(PLATFORMS)) {
            movePlattform(platform, engine);
        }
    }

    private void movePlattform(Entity platform, Engine engine) {
        var plattformComponent = platform.get(MovingPlatformComponent.class);

        if (isNull(plattformComponent.targetPosition)) {
            Entity tartetEntity = engine.entities().forcedFetchById(plattformComponent.waypoint);
            plattformComponent.targetPosition = tartetEntity.get(TransformComponent.class).bounds.position();
        }

        var transform = platform.get(TransformComponent.class);
        Vector distance = transform.bounds.position().substract(plattformComponent.targetPosition);

        if (distance.isZero()) {
            Entity currentTarget = engine.entities().forcedFetchById(plattformComponent.waypoint);
            Entity nextTarget = engine.entities()
                    .forcedFetchById(currentTarget.get(WaypointComponent.class).next);
            plattformComponent.targetPosition = nextTarget.get(TransformComponent.class).bounds.position();
            plattformComponent.waypoint = nextTarget.id().orElseThrow();
        }
        double delta = engine.loop().delta();
        double xSpeed = clamp(
                delta * -1 * plattformComponent.speed,
                -1 * distance.x(),
                delta * plattformComponent.speed);

        double ySpeed = clamp(
                delta * -1 * plattformComponent.speed,
                -1 * distance.y(),
                delta * plattformComponent.speed);

        Vector movement = Vector.of(xSpeed, ySpeed);

        var sensor = platform.get(CollisionSensorComponent.class);
        if (nonNull(sensor)) {
            for (final Entity attachedEntity : sensor.collidedEntities) {
                if (attachedEntity.hasComponent(PhysicsBodyComponent.class)) {
                    final var colliderTransform = attachedEntity.get(TransformComponent.class);
                    if (transform.bounds.minY() + 1 >= colliderTransform.bounds.maxY()) {
                        colliderTransform.bounds = colliderTransform.bounds.moveBy(movement);
                    }
                }
            }
        }
        transform.bounds = transform.bounds.moveBy(movement);
    }
}
