package dev.screwbox.core.environment.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;

import java.util.List;

@Order(Order.SystemOrder.PREPARATION)
public class NavigationGridUpdateSystem implements EntitySystem {

    private static final Archetype OBSTACLES = Archetype.ofSpacial(PhysicsGridObstacleComponent.class);

    @Override
    public void update(final Engine engine) {
        engine.environment().tryFetchSingletonComponent(PhysicsGridConfigurationComponent.class).ifPresent(config -> {
            if (config.updateScheduler.isTick(engine.loop().time())) {
                final List<Entity> obstacleEntities = engine.environment().fetchAll(OBSTACLES);
                engine.async().runExclusive(NavigationGridUpdateSystem.class, () -> {
                    final List<Bounds> obstacles = obstacleEntities.stream().map(Entity::bounds).toList();
                    engine.navigation().setNavigationRegion(config.worldBounds, config.gridSize, obstacles);
                });
            }
        });

    }
}
