package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

public class AutomovementSystem implements EntitySystem {

    private static final Archetype AUTO_MOVERS = Archetype.of(AutomovementComponent.class, PhysicsComponent.class,
            TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity mover : engine.environment().fetchAll(AUTO_MOVERS)) {
            final var automovement = mover.get(AutomovementComponent.class);
            if (automovement.path != null) {
                final Vector position = mover.position();
                if (position.distanceTo(automovement.path.lastNode()) < 1) {
                    mover.get(PhysicsComponent.class).momentum = Vector.zero();
                } else {
                    if (automovement.path.nodeCount() > 1 && position.distanceTo(automovement.path.firstNode()) < mover.bounds().extents().length()) {
                        automovement.path = automovement.path.removeNode(0);
                    }
                    MovementTargetComponent component = new MovementTargetComponent(automovement.path.firstNode());
                    component.acceleration = automovement.acceleration;
                    component.maxSpeed = automovement.speed;
                    mover.addOrReplace(component);
                    final Vector direction = automovement.path.firstNode().substract(position);
//                    mover.get(PhysicsComponent.class).momentum = direction.length(automovement.speed);
                }
            }
        }

    }

}
