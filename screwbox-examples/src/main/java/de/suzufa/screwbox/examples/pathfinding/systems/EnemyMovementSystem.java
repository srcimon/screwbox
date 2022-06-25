package de.suzufa.screwbox.examples.pathfinding.systems;

import java.util.Optional;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Path;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityEngine;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.WorldBoundsComponent;
import de.suzufa.screwbox.core.physics.Grid;
import de.suzufa.screwbox.core.utils.Timer;
import de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL.AutomovementComponent;
import de.suzufa.screwbox.examples.pathfinding.components.PlayerMovementComponent;

public class EnemyMovementSystem implements EntitySystem {

    private static final Archetype ENEMIES = Archetype.of(
            PhysicsBodyComponent.class, SpriteComponent.class,
            AutomovementComponent.class);

    private Timer t = Timer.withInterval(Duration.ofMillis(1000));

    @Override
    public void update(Engine engine) {

        Entity player = engine.entityEngine().forcedFetch(PlayerMovementComponent.class, TransformComponent.class);
        Vector playerPosition = player.get(TransformComponent.class).bounds.position();
//TODO: NO TIMER
        if (t.isTick(Time.now())) {
            Grid grid = createGrid(engine.entityEngine());
            for (Entity enemy : engine.entityEngine().fetchAll(ENEMIES)) {
                Vector enemyPosition = enemy.get(TransformComponent.class).bounds.position();

                Optional<Path> path = engine.physics().findPath(grid, enemyPosition, playerPosition);
                if (path.isPresent()) {
                    enemy.get(AutomovementComponent.class).path = path.get();
                }
            }
        }
    }

    private Grid createGrid(EntityEngine entityEngine) {
        Entity worldBounds = entityEngine.forcedFetch(WorldBoundsComponent.class,
                TransformComponent.class);
        Bounds bounds = worldBounds.get(TransformComponent.class).bounds;
        Grid grid = new Grid(bounds, 16, true);
        for (Entity entity : entityEngine
                .fetchAll(Archetype.of(ColliderComponent.class, TransformComponent.class))) {
            if (!entity.hasComponent(PhysicsBodyComponent.class)) { // TODO: add special
                grid.blockArea(entity.get(TransformComponent.class).bounds);
            }
        }
        return grid;
    }

}
