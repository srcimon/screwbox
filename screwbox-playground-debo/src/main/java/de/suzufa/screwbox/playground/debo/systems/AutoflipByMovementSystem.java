package de.suzufa.screwbox.playground.debo.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.playground.debo.components.AutoflipByMovementComponent;

public class AutoflipByMovementSystem implements EntitySystem {

    private static final Archetype FLIPPABLES = Archetype.of(AutoflipByMovementComponent.class, SpriteComponent.class,
            PhysicsBodyComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var entity : engine.entityEngine().fetchAll(FLIPPABLES)) {
            var autoflipComponent = entity.get(AutoflipByMovementComponent.class);
            var momentum = entity.get(PhysicsBodyComponent.class).momentum;
            if (momentum.x() > 0) {
                autoflipComponent.flipped = false;
            } else if (momentum.x() < 0) {
                autoflipComponent.flipped = true;
            }
            entity.get(SpriteComponent.class).sprite.setFlippedHorizontally(autoflipComponent.flipped);
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_PREPARE;
    }

}
