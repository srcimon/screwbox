package dev.screwbox.core.environment.tweening;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;

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
            renderComponent.options = renderComponent.options.spin(spin).spinHorizontal(spinComponent.isSpinHorizontal);
        }
    }
}
