package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.components.AutomovementComponent;
import io.github.srcimon.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;

public class AutomovementSystem implements EntitySystem {

    private static final Archetype AUTO_MOVERS = Archetype.of(AutomovementComponent.class, PhysicsBodyComponent.class,
            TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity mover : engine.entities().fetchAll(AUTO_MOVERS)) {
            final Vector position = mover.get(TransformComponent.class).bounds.position();
            final var automovement = mover.get(AutomovementComponent.class);
            Path path = automovement.path;

            if (path != null) {
                if (position.distanceTo(path.end()) < 1) {
                    mover.get(PhysicsBodyComponent.class).momentum = Vector.zero();
                } else {
                    if (path.nodeCount() > 1 && position.distanceTo(path.start()) < 1) {
                        path.removeNode(0);
                    }
                    final Vector direction = path.start().substract(position);
                    mover.get(PhysicsBodyComponent.class).momentum = direction.adjustLengthTo(automovement.speed);
                }

            }
        }

    }

}
