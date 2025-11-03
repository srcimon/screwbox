package dev.screwbox.platformer.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.physics.CollisionSensorComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.platformer.components.MovingPlatformComponent;
import dev.screwbox.platformer.components.WaypointComponent;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@ExecutionOrder(Order.SIMULATION_EARLY)
public class MovingPlatformSystem implements EntitySystem {

    private static final Archetype PLATFORMS = Archetype.ofSpacial(MovingPlatformComponent.class);

    @Override
    public void update(Engine engine) {
        for (var platform : engine.environment().fetchAll(PLATFORMS)) {
            movePlatform(platform, engine);
        }
    }

    private void movePlatform(Entity platform, Engine engine) {
        var platformComponent = platform.get(MovingPlatformComponent.class);

        if (isNull(platformComponent.targetPosition)) {
            Entity tartetEntity = engine.environment().fetchById(platformComponent.waypoint);
            platformComponent.targetPosition = tartetEntity.position();
        }

        Vector distance = platform.position().substract(platformComponent.targetPosition);

        if (distance.isZero()) {
            Entity currentTarget = engine.environment().fetchById(platformComponent.waypoint);
            Entity nextTarget = engine.environment()
                    .fetchById(currentTarget.get(WaypointComponent.class).next);
            platformComponent.targetPosition = nextTarget.position();
            platformComponent.waypoint = nextTarget.id().orElseThrow();
        }
        double delta = engine.loop().delta();
        double xSpeed = Math.clamp(-1 * distance.x(), delta * -1 * platformComponent.speed, delta * platformComponent.speed);

        double ySpeed = Math.clamp(-1 * distance.y(), delta * -1 * platformComponent.speed, delta * platformComponent.speed);

        Vector movement = Vector.of(xSpeed, ySpeed);

        var sensor = platform.get(CollisionSensorComponent.class);
        if (nonNull(sensor)) {
            for (final Entity attachedEntity : sensor.collidedEntities) {
                if (attachedEntity.hasComponent(PhysicsComponent.class)) {
                    final var colliderTransform = attachedEntity.get(TransformComponent.class);
                    if (platform.bounds().minY() + 1 >= colliderTransform.bounds.maxY()) {
                        colliderTransform.bounds = colliderTransform.bounds.moveBy(movement);
                    }
                }
            }
        }
        platform.moveBy(movement);
    }
}
