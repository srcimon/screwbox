package io.github.srcimon.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.platformer.components.MovingPlatformComponent;
import io.github.srcimon.screwbox.platformer.components.WaypointComponent;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Order(Order.SystemOrder.SIMULATION_EARLY)
public class MovingPlatformSystem implements EntitySystem {

    private static final Archetype PLATFORMS = Archetype.ofSpacial(MovingPlatformComponent.class);

    @Override
    public void update(Engine engine) {
        for (var platform : engine.environment().fetchAll(PLATFORMS)) {
            movePlattform(platform, engine);
        }
    }

    private void movePlattform(Entity platform, Engine engine) {
        var plattformComponent = platform.get(MovingPlatformComponent.class);

        if (isNull(plattformComponent.targetPosition)) {
            Entity tartetEntity = engine.environment().fetchById(plattformComponent.waypoint);
            plattformComponent.targetPosition = tartetEntity.position();
        }

        var transform = platform.get(TransformComponent.class);
        Vector distance = transform.bounds.position().substract(plattformComponent.targetPosition);

        if (distance.isZero()) {
            Entity currentTarget = engine.environment().fetchById(plattformComponent.waypoint);
            Entity nextTarget = engine.environment()
                    .fetchById(currentTarget.get(WaypointComponent.class).next);
            plattformComponent.targetPosition = nextTarget.position();
            plattformComponent.waypoint = nextTarget.id().orElseThrow();
        }
        double delta = engine.loop().delta();
        double xSpeed = Math.clamp(-1 * distance.x(), delta * -1 * plattformComponent.speed, delta * plattformComponent.speed);

        double ySpeed = Math.clamp(-1 * distance.y(), delta * -1 * plattformComponent.speed, delta * plattformComponent.speed);

        Vector movement = Vector.of(xSpeed, ySpeed);

        var sensor = platform.get(CollisionSensorComponent.class);
        if (nonNull(sensor)) {
            for (final Entity attachedEntity : sensor.collidedEntities) {
                if (attachedEntity.hasComponent(PhysicsComponent.class)) {
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
