package dev.screwbox.core.environment;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.physics.GravitySystem;
import dev.screwbox.core.environment.rendering.RenderSystem;
import dev.screwbox.core.graphics.Sprite;

/**
 * EntitySystems implement the game logic. Every system has a single purpose.
 * E.g. applying gravity ({@link GravitySystem}) or rendering all {@link Sprite}s ({@link RenderSystem}).
 * <p>
 * An {@link EntitySystem} can be annotated with {@link Order} to determine the order of execution.
 */
@FunctionalInterface
public interface EntitySystem {

    void update(Engine engine);

}
