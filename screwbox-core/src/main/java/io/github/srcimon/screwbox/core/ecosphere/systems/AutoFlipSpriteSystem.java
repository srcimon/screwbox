package io.github.srcimon.screwbox.core.ecosphere.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ecosphere.Archetype;
import io.github.srcimon.screwbox.core.ecosphere.EntitySystem;
import io.github.srcimon.screwbox.core.ecosphere.Order;
import io.github.srcimon.screwbox.core.ecosphere.SystemOrder;
import io.github.srcimon.screwbox.core.ecosphere.components.AutoFlipSpriteComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.PhysicsBodyComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Flip;

@Order(SystemOrder.PRESENTATION_PREPARE)
public class AutoFlipSpriteSystem implements EntitySystem {

    private static final Archetype SPRITE_BODIES = Archetype.of(
            AutoFlipSpriteComponent.class, RenderComponent.class, PhysicsBodyComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.ecosphere().fetchAll(SPRITE_BODIES)) {
            final var momentum = entity.get(PhysicsBodyComponent.class).momentum;
            if (momentum.x() > 0) {
                entity.get(RenderComponent.class).flip = Flip.NONE;
            } else if (momentum.x() < 0) {
                entity.get(RenderComponent.class).flip = Flip.HORIZONTAL;
            }
        }
    }
}
