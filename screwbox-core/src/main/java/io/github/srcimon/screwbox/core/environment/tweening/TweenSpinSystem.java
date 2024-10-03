package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Rotation;
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
            final var advance = tweenEntity.get(TweenComponent.class).value.value();
            final var renderComponent = tweenEntity.get(RenderComponent.class);
            final var spin = tweenEntity.get(TweenSpinComponent.class);
            renderComponent.options = renderComponent.options
                    .horizontalSpin(Rotation.degrees(spin.horizontal * 360 * advance))
                    .verticalSpin(Rotation.degrees(spin.vertical * 360 * advance));

        }
    }
}
