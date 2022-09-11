package de.suzufa.screwbox.core.entities.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Path;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.components.AutomovementComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;

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
