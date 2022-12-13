package de.suzufa.screwbox.examples.pathfinding.systems;

import java.util.Optional;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Path;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.components.AutomovementComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.utils.Timer;
import de.suzufa.screwbox.examples.pathfinding.components.PlayerMovementComponent;

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
                var automovement = enemy.get(AutomovementComponent.class);

                if (!engine.async().hasActiveTasks(automovement)) {
                    engine.async().run(automovement, () -> {
                        Optional<Path> path = engine.physics().findPath(enemyPosition, playerPosition);
                        if (path.isPresent()) {
                            automovement.path = path.get();
                        }
                    });
                }
            }
        }
    }

}
