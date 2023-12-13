package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.graphics.Flip;

@Order(SystemOrder.PRESENTATION_PREPARE)
public class FlipSpriteSystem implements EntitySystem {

    private static final Archetype SPRITE_BODIES = Archetype.of(
            FlipSpriteComponent.class, RenderComponent.class, PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(SPRITE_BODIES)) {
            final var momentum = entity.get(PhysicsComponent.class).momentum;
            if (momentum.x() > 0) {
                entity.get(RenderComponent.class).flip = Flip.NONE;
            } else if (momentum.x() < 0) {
                entity.get(RenderComponent.class).flip = Flip.HORIZONTAL;
            }
        }
    }
}
