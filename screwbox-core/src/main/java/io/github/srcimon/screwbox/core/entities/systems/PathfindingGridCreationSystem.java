package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Grid;
import io.github.srcimon.screwbox.core.entities.components.PathfindingBlockingComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.entities.components.WorldBoundsComponent;
import io.github.srcimon.screwbox.core.utils.Sheduler;
import io.github.srcimon.screwbox.core.entities.*;

@Order(SystemOrder.PREPARATION)
public class PathfindingGridCreationSystem implements EntitySystem {

    private static final Archetype BLOCKING = Archetype.of(
            PathfindingBlockingComponent.class, TransformComponent.class);

    private static final Archetype WORLD = Archetype.of(WorldBoundsComponent.class, TransformComponent.class);

    private final int gridSize;
    private final Sheduler updateSheduler;

    public PathfindingGridCreationSystem(final int gridSize, final Sheduler updateSheduler) {
        this.gridSize = gridSize;
        this.updateSheduler = updateSheduler;
    }

    @Override
    public void update(final Engine engine) {
        if (updateSheduler.isTick(engine.loop().lastUpdate())) {
            final Bounds bounds = engine.entities().forcedFetch(WORLD).get(TransformComponent.class).bounds;

            final Grid grid = new Grid(bounds, gridSize);
            for (final Entity blocking : engine.entities().fetchAll(BLOCKING)) {
                grid.blockArea(blocking.get(TransformComponent.class).bounds);
            }
            engine.physics().setGrid(grid);
        }
    }
}
