package io.github.srcimon.screwbox.pathfinding.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.AutomovementComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.utils.Sheduler;
import io.github.srcimon.screwbox.pathfinding.components.PlayerMovementComponent;

public class EnemyMovementSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(
            PlayerMovementComponent.class, TransformComponent.class);

    private static final Archetype ENEMIES = Archetype.of(
            PhysicsComponent.class, RenderComponent.class, AutomovementComponent.class);

    private final Sheduler sheduler = Sheduler.everySecond();

    @Override
    public void update(final Engine engine) {
        if (sheduler.isTick(engine.loop().lastUpdate())) {
            final Entity player = engine.environment().fetchSingleton(PLAYER);
            final Vector playerPosition = player.position();
            for (final Entity enemy : engine.environment().fetchAll(ENEMIES)) {
                final Vector enemyPosition = enemy.position();
                final var automovement = enemy.get(AutomovementComponent.class);
                engine.async().runExclusive(automovement, () -> engine.physics().findPath(enemyPosition, playerPosition).ifPresent(value -> automovement.path = value));
            }
        }
    }

}
