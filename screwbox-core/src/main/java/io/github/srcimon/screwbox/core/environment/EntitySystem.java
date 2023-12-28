package io.github.srcimon.screwbox.core.environment;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.physics.GravitySystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderSystem;
import io.github.srcimon.screwbox.core.graphics.Sprite;

/**
 * EntitySystems implement the game logic. Every system has a single purpose.
 * E.g. applying gravity ({@link GravitySystem}) or rendering all {@link Sprite}s ({@link RenderSystem}).
 * <p>
 * An {@link EntitySystem} can be annotated with {@link SystemOrder} to determin the order of execution.
 */
@FunctionalInterface
public interface EntitySystem {

    void update(Engine engine);

}
