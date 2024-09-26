package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Grid;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

@Order(Order.SystemOrder.PREPARATION)
public class PhysicsGridUpdateSystem implements EntitySystem {

    private static final Archetype OBSTACLES = Archetype.of(PhysicsGridObstacleComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        engine.environment().tryFetchSingletonComponent(PhysicsGridConfigurationComponent.class).ifPresent(config -> {
            if (config.updateSheduler.isTick(engine.loop().lastUpdate())) {
                final Grid grid = new Grid(config.worldBounds, config.gridSize);
                for (final Entity obstacle : engine.environment().fetchAll(OBSTACLES)) {
                    grid.blockArea(obstacle.bounds());
                }
                engine.physics().setGrid(grid);
            }
        });
    }
}
