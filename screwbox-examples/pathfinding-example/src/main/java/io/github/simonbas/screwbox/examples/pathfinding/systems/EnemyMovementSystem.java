package io.github.simonbas.screwbox.examples.pathfinding.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Vector;
import io.github.simonbas.screwbox.core.entities.Archetype;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.entities.components.AutomovementComponent;
import io.github.simonbas.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.core.utils.Timer;
import io.github.simonbas.screwbox.examples.pathfinding.components.PlayerMovementComponent;

public class EnemyMovementSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(
            PlayerMovementComponent.class, TransformComponent.class);

    private static final Archetype ENEMIES = Archetype.of(
            PhysicsBodyComponent.class, RenderComponent.class, AutomovementComponent.class);

    private final Timer timer = Timer.everySecond();

    @Override
    public void update(final Engine engine) {
        if (timer.isTick(engine.loop().lastUpdate())) {
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
