package dev.screwbox.core.environment.ai;

import dev.screwbox.core.Engine;
import dev.screwbox.core.navigation.Path;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

import static java.util.Objects.nonNull;

/**
 * Moves {@link Entity entities} containing {@link PathMovementComponent} along the {@link Path}.
 */
public class PathMovementSystem implements EntitySystem {

    private static final Archetype AUTO_MOVERS = Archetype.ofSpacial(PathMovementComponent.class, PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity mover : engine.environment().fetchAll(AUTO_MOVERS)) {
            final var movement = mover.get(PathMovementComponent.class);
            if (nonNull(movement.path)) {
                if (mover.position().distanceTo(movement.path.end()) < 1) {
                    mover.get(PhysicsComponent.class).velocity = Vector.zero();
                    mover.remove(TargetMovementComponent.class);
                } else {
                    if (movement.path.nodeCount() > 1 && mover.position().distanceTo(movement.path.start()) < mover.bounds().extents().length()) {
                        movement.path = movement.path.removeNode(0);
                    }
                    mover.remove(TargetMovementComponent.class);
                    mover.add(new TargetMovementComponent(movement.path.start()), target -> {
                        target.acceleration = movement.acceleration;
                        target.maxSpeed = movement.speed;
                    });
                }
            }
        }
    }
}
