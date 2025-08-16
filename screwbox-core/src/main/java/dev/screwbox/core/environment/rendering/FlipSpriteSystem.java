package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.PhysicsComponent;

@Order(Order.SystemOrder.PRESENTATION_PREPARE)
public class FlipSpriteSystem implements EntitySystem {

    private static final Archetype SPRITE_BODIES = Archetype.of(
            FlipSpriteComponent.class, RenderComponent.class, PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(SPRITE_BODIES)) {
            final var velocity = entity.get(PhysicsComponent.class).velocity;
            final var renderComponent = entity.get(RenderComponent.class);
            if (velocity.x() > 0) {
                renderComponent.options = renderComponent.options.flipHorizontal(false);
            } else if (velocity.x() < 0) {
                renderComponent.options = renderComponent.options.flipHorizontal(true);
            }
        }
    }
}
