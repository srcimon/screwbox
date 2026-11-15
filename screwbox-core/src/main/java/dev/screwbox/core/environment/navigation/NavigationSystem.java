package dev.screwbox.core.environment.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

/**
 * Will constantly update the {@link Navigation#navigationRegion()} to enable pathfinding.
 */
@ExecutionOrder(Order.PREPARATION)
public class NavigationSystem implements EntitySystem {

    private static final Archetype OBSTACLES = Archetype.ofSpacial(ObstacleComponent.class);
    private static final Archetype AVOIDANCE = Archetype.ofSpacial(AvoidanceComponent.class);

    @Override
    public void update(final Engine engine) {
        if (engine.async().hasNoActiveTask(NavigationSystem.class)) {
            engine.environment().tryFetchSingleton(NavigationRegionComponent.class).ifPresent(region -> {
                if (region.get(NavigationRegionComponent.class).updateInterval.isTick(engine.loop().time())) {
                    final List<Entity> obstacleEntities = engine.environment().fetchAll(OBSTACLES);
                    final List<Entity> avoidanceEntities = engine.environment().fetchAll(AVOIDANCE);
                    engine.async().run(NavigationSystem.class, () -> {
                        final List<Bounds> obstacles = entitiesToBounds(obstacleEntities);
                        final List<Bounds> avoidances = entitiesToBounds(avoidanceEntities);
                        engine.navigation().setNavigationRegion(region.bounds(), obstacles, avoidances);
                    });
                }
            });
        }
    }

    private static List<Bounds> entitiesToBounds(final List<Entity> obstacleEntities) {
        final List<Bounds> obstacles = new ArrayList<>();
        for(var entity : obstacleEntities) {
            obstacles.add(entity.bounds());
        }
        return obstacles;
    }
}
