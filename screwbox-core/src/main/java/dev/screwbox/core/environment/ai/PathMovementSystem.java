package dev.screwbox.core.environment.ai;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Moves {@link Entity entities} containing {@link PathMovementComponent} along the {@link Polygon}.
 */
public class PathMovementSystem implements EntitySystem {

    private static final Archetype MOVERS = Archetype.ofSpacial(PathMovementComponent.class, PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity mover : engine.environment().fetchAll(MOVERS)) {
            final var movement = mover.get(PathMovementComponent.class);
            if (nonNull(movement.targetId) && movement.scheduler.isTick(engine.loop().time())) {
                engine.environment().tryFetchById(movement.targetId).ifPresent(
                    targetEntity -> engine.async().runExclusive(movement, () -> movement.path = engine.navigation().findPath(mover.position(), targetEntity.position()).orElse(null)));
            }

            if (nonNull(movement.path)) {
                movement.path = updateMover(mover, movement.path, movement.acceleration, movement.speed);
            }
        }
    }

    private static Polygon updateMover(final Entity mover, final Polygon path, final double acceleration, final double speed) {
        // has reached last target
        if (mover.position().distanceTo(path.lastNode()) < 1) {
            mover.get(PhysicsComponent.class).velocity = Vector.zero();
            mover.remove(TargetMovementComponent.class);
            return path;
        }

        // check if near target
        final var updatedPath = path.nodeCount() > 1 && mover.position().distanceTo(path.firstNode()) < mover.bounds().extents().length()
            ? path.removeNode(0)
            : path;

        // update next target
        final var targetMovement = mover.get(TargetMovementComponent.class);
        if (isNull(targetMovement)) {
            mover.add(new TargetMovementComponent(updatedPath.firstNode()), target -> {
                target.acceleration = acceleration;
                target.maxSpeed = speed;
            });
        } else {
            targetMovement.position = updatedPath.firstNode();
            targetMovement.acceleration = acceleration;
            targetMovement.maxSpeed = speed;
        }
        return updatedPath;
    }
}
