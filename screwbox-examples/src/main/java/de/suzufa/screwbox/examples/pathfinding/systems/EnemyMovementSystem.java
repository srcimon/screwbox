package de.suzufa.screwbox.examples.pathfinding.systems;

import static de.suzufa.screwbox.core.Duration.ofSeconds;

import java.util.Optional;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Path;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.components.AutomovementComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.utils.Timer;
import de.suzufa.screwbox.examples.pathfinding.components.PlayerMovementComponent;

public class EnemyMovementSystem implements EntitySystem {

    private static final Archetype PLAYER = Archetype.of(PlayerMovementComponent.class, TransformComponent.class);
    private static final Archetype ENEMIES = Archetype.of(
            PhysicsBodyComponent.class, SpriteComponent.class,
            AutomovementComponent.class);

    private final Timer timer = Timer.withInterval(ofSeconds(1));

    @Override
    public void update(final Engine engine) {
        if (timer.isTick(engine.loop().lastUpdate())) {
            final Entity player = engine.entities().forcedFetch(PLAYER);
            final Vector playerPosition = player.get(TransformComponent.class).bounds.position();
            for (final Entity enemy : engine.entities().fetchAll(ENEMIES)) {
                final Vector enemyPosition = enemy.get(TransformComponent.class).bounds.position();
                Optional<Path> path = engine.physics().findPath(enemyPosition, playerPosition);
                if (path.isPresent()) {
                    var automovement = enemy.get(AutomovementComponent.class);
                    automovement.path = path.get();
                }
            }
        }
    }

}
