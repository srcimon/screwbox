package de.suzufa.screwbox.core.entityengine.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Path;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.components.AutomovementComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;

public class AutomovementSystem implements EntitySystem {

    private static final Archetype AUTO_MOVERS = Archetype.of(AutomovementComponent.class, PhysicsBodyComponent.class,
            TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity mover : engine.entityEngine().fetchAll(AUTO_MOVERS)) {
            final Vector position = mover.get(TransformComponent.class).bounds.position();
            final var automovement = mover.get(AutomovementComponent.class);
            Path path = automovement.path;

            if (path != null) {
                if (position.distanceTo(path.end()) < 1) {
                    mover.get(PhysicsBodyComponent.class).momentum = Vector.zero();
                } else {
                    if (path.nodeCount() > 1 && position.distanceTo(path.start()) < 1) {
                        path.dropStart();
                    }
                    final Vector direction = path.start().substract(position);
                    mover.get(PhysicsBodyComponent.class).momentum = direction.capToLength(automovement.speed);
                }

            }
        }

    }

}
