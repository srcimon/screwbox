package de.suzufa.screwbox.playground.debo.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.graphics.FlipMode;
import de.suzufa.screwbox.playground.debo.components.AutoflipByMovementComponent;

public class AutoflipByMovementSystem implements EntitySystem {

    private static final Archetype FLIPPABLES = Archetype.of(AutoflipByMovementComponent.class, SpriteComponent.class,
            PhysicsBodyComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.entityEngine().fetchAll(FLIPPABLES)) {
            final var momentum = entity.get(PhysicsBodyComponent.class).momentum;
            if (momentum.x() > 0) {
                entity.get(SpriteComponent.class).flipMode = FlipMode.HORIZONTAL;
            } else if (momentum.x() < 0) {
                entity.get(SpriteComponent.class).flipMode = FlipMode.NONE;
            }
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_PREPARE;
    }

}
