package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Grid;
import io.github.srcimon.screwbox.core.environment.components.TransformComponent;
import io.github.srcimon.screwbox.core.environment.components.WorldBoundsComponent;
import io.github.srcimon.screwbox.core.utils.Sheduler;
import io.github.srcimon.screwbox.core.environment.*;

@Order(SystemOrder.PREPARATION)
public class PathfindingSystem implements EntitySystem {

    private static final Archetype BLOCKING = Archetype.of(
            BlockPathComponent.class, TransformComponent.class);

    private static final Archetype WORLD = Archetype.of(WorldBoundsComponent.class, TransformComponent.class);

    private final int gridSize;
    private final Sheduler updateSheduler;

    public PathfindingSystem(final int gridSize, final Sheduler updateSheduler) {
        this.gridSize = gridSize;
        this.updateSheduler = updateSheduler;
    }

    @Override
    public void update(final Engine engine) {
        if (updateSheduler.isTick(engine.loop().lastUpdate())) {
            final Bounds bounds = engine.environment().forcedFetch(WORLD).get(TransformComponent.class).bounds;

            final Grid grid = new Grid(bounds, gridSize);
            for (final Entity blocking : engine.environment().fetchAll(BLOCKING)) {
                grid.blockArea(blocking.get(TransformComponent.class).bounds);
            }
            engine.physics().setGrid(grid);
        }
    }
}
