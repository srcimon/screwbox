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
    public void update(Engine engine) {
        for (Entity entity : engine.entityEngine().fetchAll(MOVINGS)) {
            Vector position = entity.get(TransformComponent.class).bounds.position();
            var automovement = entity.get(AutomovementComponent.class);
            if (automovement.path != null) {
                double d = 8; // TODO: engine.physics().gridsize() / 2;

                if (position.distanceTo(automovement.path.end()) < d) {
                    entity.get(PhysicsBodyComponent.class).momentum = Vector.zero();
                } else {
                    if (automovement.path.nodeCount() > 1 && position.distanceTo(automovement.path.start()) < d) {
                        automovement.path.dropFirstNode();
                    }
                    Vector direction = automovement.path.start().substract(position);
                    entity.get(PhysicsBodyComponent.class).momentum = direction.capLength(automovement.speed);
                }

            }
        }

    }

}
