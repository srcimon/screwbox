package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.Order;
import io.github.srcimon.screwbox.core.entities.SystemOrder;
import io.github.srcimon.screwbox.core.entities.components.AutoFlipSpriteComponent;
import io.github.srcimon.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.srcimon.screwbox.core.entities.components.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Flip;

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
