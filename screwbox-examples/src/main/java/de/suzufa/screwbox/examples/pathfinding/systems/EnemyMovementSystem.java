package de.suzufa.screwbox.examples.pathfinding.systems;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.components.AutomovementComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.utils.Timer;
import de.suzufa.screwbox.examples.pathfinding.components.PlayerMovementComponent;

public class EnemyMovementSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMovementComponent.class, TransformComponent.class);
    private static final Archetype ENEMIES = Archetype.of(
            PhysicsBodyComponent.class, SpriteComponent.class,
            AutomovementComponent.class);

    private final Timer timer = Timer.withInterval(Duration.ofMillis(1000));

    @Override
    public void update(final Engine engine) {
        if (timer.isTick(engine.loop().metrics().timeOfLastUpdate())) {
            final Entity player = engine.entityEngine().forcedFetch(PLAYER);
            final Vector playerPosition = player.get(TransformComponent.class).bounds.position();
            for (final Entity enemy : engine.entityEngine().fetchAll(ENEMIES)) {
                final Vector enemyPosition = enemy.get(TransformComponent.class).bounds.position();

                var automovement = enemy.get(AutomovementComponent.class);
                engine.physics().findPathAsync(enemyPosition, playerPosition, (path) -> automovement.path = path);
            }
        }
    }

}
