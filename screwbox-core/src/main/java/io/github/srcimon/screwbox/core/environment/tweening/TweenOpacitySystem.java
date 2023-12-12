package io.github.srcimon.screwbox.core.environment.tweening;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.components.RenderComponent;

/**
 * Updates the opacity of all {@link Entity}s that use tweening and have an {@link TweenOpacityComponent}.
 */
public class TweenOpacitySystem implements EntitySystem {

    private static final Archetype TWEENS = Archetype.of(TweenStateComponent.class, TweenOpacityComponent.class, RenderComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var tween : engine.environment().fetchAll(TWEENS)) {
            tween.get(RenderComponent.class).opacity = tween.get(TweenStateComponent.class).progress;
        }
    }
}