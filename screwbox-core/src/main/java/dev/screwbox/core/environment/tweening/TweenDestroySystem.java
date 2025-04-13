package dev.screwbox.core.environment.tweening;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;

/**
 * Removes all {@link Entity}s that have ended tweening and have an {@link TweenDestroyComponent}.
 */
public class TweenDestroySystem implements EntitySystem {

    private static final Archetype DESTROYABLES = Archetype.of(TweenDestroyComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var destroyable : engine.environment().fetchAll(DESTROYABLES)) {
            if (!destroyable.hasComponent(TweenComponent.class)) {
                engine.environment().remove(destroyable);
            }
        }
    }
}
