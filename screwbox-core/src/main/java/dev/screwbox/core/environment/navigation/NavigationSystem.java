package dev.screwbox.core.environment.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.navigation.Navigation;

import java.util.List;

/**
 * Will constantly update the {@link Navigation#navigationRegion()} to enable pathfinding.
 */
@ExecutionOrder(Order.PREPARATION)
public class NavigationSystem implements EntitySystem {

    private static final Archetype OBSTACLES = Archetype.ofSpacial(ObstacleComponent.class);

    @Override
    public void update(final Engine engine) {
        if (engine.async().hasNoActiveTask(NavigationSystem.class)) {
            engine.environment().tryFetchSingleton(NavigationRegionComponent.class).ifPresent(region -> {
                if (region.get(NavigationRegionComponent.class).updateInterval.isTick(engine.loop().time())) {
                    final List<Entity> obstacleEntities = engine.environment().fetchAll(OBSTACLES);
                    engine.async().run(NavigationSystem.class, () -> {
                        final List<Bounds> obstacles = obstacleEntities.stream().map(Entity::bounds).toList();
                        engine.navigation().setNavigationRegion(region.bounds(), obstacles);
                    });
                }
            });
        }
    }
}
