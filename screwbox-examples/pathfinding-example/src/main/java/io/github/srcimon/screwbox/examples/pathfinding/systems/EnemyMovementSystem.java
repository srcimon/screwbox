package io.github.srcimon.screwbox.examples.pathfinding.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.components.AutomovementComponent;
import io.github.srcimon.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.srcimon.screwbox.core.entities.components.RenderComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.utils.Sheduler;
import io.github.srcimon.screwbox.examples.pathfinding.components.PlayerMovementComponent;

public class EnemyMovementSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(
            PlayerMovementComponent.class, TransformComponent.class);

    private static final Archetype ENEMIES = Archetype.of(
            PhysicsBodyComponent.class, RenderComponent.class, AutomovementComponent.class);

    private final Sheduler sheduler = Sheduler.everySecond();

    @Override
    public void update(final Engine engine) {
        if (sheduler.isTick(engine.loop().lastUpdate())) {
            final Entity player = engine.entities().forcedFetch(PLAYER);
            final Vector playerPosition = player.get(TransformComponent.class).bounds.position();
            for (final Entity enemy : engine.entities().fetchAll(ENEMIES)) {
                final Vector enemyPosition = enemy.get(TransformComponent.class).bounds.position();
                final var automovement = enemy.get(AutomovementComponent.class);
                engine.async().runSingle(automovement, () -> engine.physics().findPath(enemyPosition, playerPosition).ifPresent(value -> automovement.path = value));
            }
        }
    }

}
