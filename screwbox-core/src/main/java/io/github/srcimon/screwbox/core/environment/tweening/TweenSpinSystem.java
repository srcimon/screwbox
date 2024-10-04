package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;

//TODO test
/**
 * Updates the {@link SpriteDrawOptions#spin()} and {@link SpriteDrawOptions#isSpinHorizontal()} of all
 * {@link Entity entities} that use tweening and have an {@link TweenOpacityComponent}.
 */
@Order(Order.SystemOrder.PRESENTATION_PREPARE)
public class TweenSpinSystem implements EntitySystem {

    private static final Archetype TWEENS = Archetype.of(TweenComponent.class, TweenSpinComponent.class, RenderComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var tweenEntity : engine.environment().fetchAll(TWEENS)) {
            final var renderComponent = tweenEntity.get(RenderComponent.class);
            final var spinComponent = tweenEntity.get(TweenSpinComponent.class);
            final var spin = tweenEntity.get(TweenComponent.class).value;
            renderComponent.options = renderComponent.options.spin(spin).isSpinHorizontal(spinComponent.isSpinHorizontal);
        }
    }
}
