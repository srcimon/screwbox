package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;

/**
 * Updates the {@link SpriteDrawOptions#opacity()} of all {@link Entity entities} that use tweening and have an
 * {@link TweenOpacityComponent}.
 */
@Order(Order.SystemOrder.PRESENTATION_PREPARE)
public class TweenOpacitySystem implements EntitySystem {

    private static final Archetype TWEENS = Archetype.of(TweenComponent.class, TweenOpacityComponent.class, RenderComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var tweenEntity : engine.environment().fetchAll(TWEENS)) {
            final var opacityComponent = tweenEntity.get(TweenOpacityComponent.class);
            final var newOpacity = tweenEntity.get(TweenComponent.class).value.rangeValue(opacityComponent.from.value(), opacityComponent.to.value());
            RenderComponent renderComponent = tweenEntity.get(RenderComponent.class);
            renderComponent.options = renderComponent.options.opacity(newOpacity);
        }
    }
}