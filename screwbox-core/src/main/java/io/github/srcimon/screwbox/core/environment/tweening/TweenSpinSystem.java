package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;

//TODO javadoc
//TODO changelog
//TODO test
public class TweenSpinSystem implements EntitySystem {

    private static final Archetype TWEENS = Archetype.of(TweenComponent.class, TweenSpinComponent.class, RenderComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var tweenEntity : engine.environment().fetchAll(TWEENS)) {
            final var renderComponent = tweenEntity.get(RenderComponent.class);
            final var spinComponent = tweenEntity.get(TweenSpinComponent.class);
            final var spin = tweenEntity.get(TweenComponent.class).value;
            renderComponent.options = renderComponent.options.spin(spin).isSpinHorizontal(spinComponent.isSpinHorizontal);
        }
    }
}
