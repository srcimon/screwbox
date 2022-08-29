package de.suzufa.screwbox.core.entityengine.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.UpdatePriority;
import de.suzufa.screwbox.core.entityengine.components.AutoFlipSpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.graphics.FlipMode;

public class AutFlipSpriteSystem implements EntitySystem {

    private static final Archetype SPRITE_BODIES = Archetype.of(
            AutoFlipSpriteComponent.class, SpriteComponent.class, PhysicsBodyComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.entityEngine().fetchAll(SPRITE_BODIES)) {
            final var momentum = entity.get(PhysicsBodyComponent.class).momentum;
            if (momentum.x() > 0) {
                entity.get(SpriteComponent.class).flipMode = FlipMode.NONE;
            } else if (momentum.x() < 0) {
                entity.get(SpriteComponent.class).flipMode = FlipMode.HORIZONTAL;
            }
        }
    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_PREPARE;
    }

}
