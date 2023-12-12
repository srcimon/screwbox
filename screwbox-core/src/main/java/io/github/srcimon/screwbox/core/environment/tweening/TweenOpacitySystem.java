package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.components.RenderComponent;

//TODO: javadoc and tests
public class TweenOpacitySystem implements EntitySystem {

    private static final Archetype TWEENS = Archetype.of(TweenState.class, TweenOpacity.class, RenderComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var tween : engine.environment().fetchAll(TWEENS)) {
            final var progress = tween.get(TweenState.class).progress;
            tween.get(RenderComponent.class).opacity = progress;
        }
    }
}