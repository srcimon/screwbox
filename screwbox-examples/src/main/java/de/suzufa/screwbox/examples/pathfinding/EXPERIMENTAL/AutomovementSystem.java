package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;

public class AutomovementSystem implements EntitySystem {

    private static final Archetype MOVINGS = Archetype.of(AutomovementComponent.class, PhysicsBodyComponent.class,
            TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.entityEngine().fetchAll(MOVINGS)) {
            final Vector position = entity.get(TransformComponent.class).bounds.position();
            final var automovement = entity.get(AutomovementComponent.class);
            if (automovement.path != null) {
                if (position.distanceTo(automovement.path.end()) < 1) {
                    entity.get(PhysicsBodyComponent.class).momentum = Vector.zero();
                } else {
                    if (automovement.path.nodeCount() > 1 && position.distanceTo(automovement.path.start()) < 1) {
                        automovement.path.dropStart();
                    }
                    final Vector direction = automovement.path.start().substract(position);
                    entity.get(PhysicsBodyComponent.class).momentum = direction.capLength(automovement.speed);
                }

            }
        }

    }

}
