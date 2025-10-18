package dev.screwbox.core.environment.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;

import java.util.List;

@Order(Order.SystemOrder.PREPARATION)
public class NavigationSystem implements EntitySystem {

    private static final Archetype OBSTACLES = Archetype.ofSpacial(ObstacleComponent.class);

    @Override
    public void update(final Engine engine) {
        engine.environment().tryFetchSingleton(NavigationRegionComponent.class).ifPresent(entity -> {
            final var config = entity.get(NavigationRegionComponent.class);
            if (config.updateScheduler.isTick(engine.loop().time())) {
                final List<Entity> obstacleEntities = engine.environment().fetchAll(OBSTACLES);
                engine.async().runExclusive(NavigationSystem.class, () -> {
                    final List<Bounds> obstacles = obstacleEntities.stream().map(Entity::bounds).toList();
                    engine.navigation().setNavigationRegion(entity.bounds(), obstacles);
                });
            }
        });

    }
}
