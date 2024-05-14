package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;

/**
 * Updates the {@link SpriteDrawOptions#scale()} of all {@link Entity}s that use tweening and have an {@link TweenScaleComponent}.
 */
@Order(Order.SystemOrder.PRESENTATION_PREPARE)
public class TweenScaleSystem implements EntitySystem {

    private static final Archetype TWEENS = Archetype.of(TweenComponent.class, TweenScaleComponent.class, RenderComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var tweenEntity : engine.environment().fetchAll(TWEENS)) {
            final var scaleComponent = tweenEntity.get(TweenScaleComponent.class);
            final var advance = (scaleComponent.to - scaleComponent.from) * tweenEntity.get(TweenComponent.class).value.value();
            RenderComponent renderComponent = tweenEntity.get(RenderComponent.class);
            renderComponent.options = renderComponent.options.scale(scaleComponent.from + advance);
        }
    }
}