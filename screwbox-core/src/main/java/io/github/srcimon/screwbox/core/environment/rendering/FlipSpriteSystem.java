package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

@Order(Order.SystemOrder.PRESENTATION_PREPARE)
public class FlipSpriteSystem implements EntitySystem {

    private static final Archetype SPRITE_BODIES = Archetype.of(
            FlipSpriteComponent.class, RenderComponent.class, PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(SPRITE_BODIES)) {
            final var momentum = entity.get(PhysicsComponent.class).momentum;
            RenderComponent renderComponent = entity.get(RenderComponent.class);
            if (momentum.x() > 0) {
                renderComponent.options = renderComponent.options.flipHorizontal(false);
            } else if (momentum.x() < 0) {
                renderComponent.options = renderComponent.options.flipHorizontal(true);
            }
        }
    }
}
