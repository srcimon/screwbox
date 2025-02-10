package io.github.srcimon.screwbox.pathfinding.systems;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.ai.PathMovementComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.utils.Scheduler;
import io.github.srcimon.screwbox.pathfinding.components.PlayerMovementComponent;

public class EnemyMovementSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.ofSpacial(PlayerMovementComponent.class);

    private static final Archetype ENEMIES = Archetype.of(
            PhysicsComponent.class, RenderComponent.class, PathMovementComponent.class);

    private final Scheduler scheduler = Scheduler.withInterval(Duration.ofMillis(250));

    @Override
    public void update(final Engine engine) {
        if (scheduler.isTick(engine.loop().time())) {
            final Entity player = engine.environment().fetchSingleton(PLAYER);
            final Vector playerPosition = player.position();
            for (final Entity enemy : engine.environment().fetchAll(ENEMIES)) {
                final Vector enemyPosition = enemy.position();
                final var automovement = enemy.get(PathMovementComponent.class);
                engine.async().runExclusive(automovement, () -> engine.physics().findPath(enemyPosition, playerPosition).ifPresent(value -> automovement.path = value));
            }
        }
    }

}
