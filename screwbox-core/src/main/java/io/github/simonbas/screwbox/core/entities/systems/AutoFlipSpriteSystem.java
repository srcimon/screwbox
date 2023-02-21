package io.github.simonbas.screwbox.core.entities.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.Archetype;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.entities.Order;
import io.github.simonbas.screwbox.core.entities.SystemOrder;
import io.github.simonbas.screwbox.core.entities.components.AutoFlipSpriteComponent;
import io.github.simonbas.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.graphics.Flip;

@Order(SystemOrder.PRESENTATION_PREPARE)
public class AutoFlipSpriteSystem implements EntitySystem {

    private static final Archetype SPRITE_BODIES = Archetype.of(
            AutoFlipSpriteComponent.class, RenderComponent.class, PhysicsBodyComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.entities().fetchAll(SPRITE_BODIES)) {
            final var momentum = entity.get(PhysicsBodyComponent.class).momentum;
            if (momentum.x() > 0) {
                entity.get(RenderComponent.class).flip = Flip.NONE;
            } else if (momentum.x() < 0) {
                entity.get(RenderComponent.class).flip = Flip.HORIZONTAL;
            }
        }
    }
}
