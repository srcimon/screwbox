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
            if (automovement.path != null && !automovement.path.isEmpty()) {
                if (position.distanceTo(automovement.path.get(0)) < 10) {
                    automovement.path.remove(0);
                }
                Vector direction = automovement.path.get(0).substract(position);

                entity.get(PhysicsBodyComponent.class).momentum = direction.capLength(automovement.speed);
                // TODO: if near target remove first dot
            }
        }

    }

}
