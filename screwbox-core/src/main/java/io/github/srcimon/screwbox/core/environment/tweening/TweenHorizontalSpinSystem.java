package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;

//TODO javadoc
//TODO changelog
//TODO test
public class TweenHorizontalSpinSystem implements EntitySystem {

    private static final Archetype TWEENS = Archetype.of(TweenComponent.class, TweenHorizontalSpinComponent.class, RenderComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var tweenEntity : engine.environment().fetchAll(TWEENS)) {
            final var advance = tweenEntity.get(TweenComponent.class).value.value();
            RenderComponent renderComponent = tweenEntity.get(RenderComponent.class);
            renderComponent.options = renderComponent.options.horizontalSpin(Rotation.degrees(360 * advance));
        }
    }
}
