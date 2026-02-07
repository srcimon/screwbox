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
 * Updates the {@link SpriteDrawOptions#scale()} of all {@link Entity}s that use tweening and have an {@link TweenScaleComponent}.
 */
@ExecutionOrder(Order.PRESENTATION_PREPARE)
public class TweenScaleSystem implements EntitySystem {

    private static final Archetype TWEENS = Archetype.of(TweenComponent.class, TweenScaleComponent.class, RenderComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var tweenEntity : engine.environment().fetchAll(TWEENS)) {
            final var scaleComponent = tweenEntity.get(TweenScaleComponent.class);
            final var newScale = tweenEntity.get(TweenComponent.class).value.rangeValue(scaleComponent.from, scaleComponent.to);
            RenderComponent renderComponent = tweenEntity.get(RenderComponent.class);
            renderComponent.options = renderComponent.options.scale(newScale);
        }
    }
}