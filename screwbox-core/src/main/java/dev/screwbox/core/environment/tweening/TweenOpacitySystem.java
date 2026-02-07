package dev.screwbox.core.environment.tweening;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;

/**
 * Updates the {@link SpriteDrawOptions#opacity()} of all {@link Entity entities} that use tweening and have an
 * {@link TweenOpacityComponent}.
 */
@ExecutionOrder(Order.PRESENTATION_PREPARE)
public class TweenOpacitySystem implements EntitySystem {

    private static final Archetype TWEENS = Archetype.of(TweenComponent.class, TweenOpacityComponent.class, RenderComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var tweenEntity : engine.environment().fetchAll(TWEENS)) {
            final var opacityComponent = tweenEntity.get(TweenOpacityComponent.class);
            final var newOpacity = tweenEntity.get(TweenComponent.class).value.rangeValue(opacityComponent.from.value(), opacityComponent.to.value());
            RenderComponent renderComponent = tweenEntity.get(RenderComponent.class);
            renderComponent.options = renderComponent.options.opacity(newOpacity);
        }
    }
}