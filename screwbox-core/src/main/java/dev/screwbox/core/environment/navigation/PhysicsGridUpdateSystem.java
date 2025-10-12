package dev.screwbox.core.environment.navigation;

import dev.screwbox.core.Engine;
import dev.screwbox.core.navigation.Grid;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;

@Order(Order.SystemOrder.PREPARATION)
public class PhysicsGridUpdateSystem implements EntitySystem {

    private static final Archetype OBSTACLES = Archetype.ofSpacial(PhysicsGridObstacleComponent.class);

    @Override
    public void update(final Engine engine) {
        engine.environment().tryFetchSingletonComponent(PhysicsGridConfigurationComponent.class).ifPresent(config -> {
            if (config.updateScheduler.isTick(engine.loop().time())) {
                final Grid grid = new Grid(config.worldBounds, config.gridSize);
                for (final Entity obstacle : engine.environment().fetchAll(OBSTACLES)) {
                    grid.blockArea(obstacle.bounds());
                }
                engine.physics().setGrid(grid);
            }
        });
    }
}
