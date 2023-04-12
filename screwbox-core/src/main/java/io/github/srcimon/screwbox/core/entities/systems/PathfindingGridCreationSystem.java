package io.github.srcimon.screwbox.core.entities.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Grid;
import io.github.srcimon.screwbox.core.entities.components.PathfindingBlockingComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.entities.components.WorldBoundsComponent;
import io.github.srcimon.screwbox.core.utils.Timer;
import io.github.srcimon.screwbox.core.entities.*;

@Order(SystemOrder.PREPARATION)
public class PathfindingGridCreationSystem implements EntitySystem {

    private static final Archetype BLOCKING = Archetype.of(
            PathfindingBlockingComponent.class, TransformComponent.class);

    private static final Archetype WORLD = Archetype.of(WorldBoundsComponent.class, TransformComponent.class);

    private final int gridSize;
    private final Timer updateTimer;

    public PathfindingGridCreationSystem(final int gridSize, final Timer updateTimer) {
        this.gridSize = gridSize;
        this.updateTimer = updateTimer;
    }

    @Override
    public void update(final Engine engine) {
        if (updateTimer.isTick(engine.loop().lastUpdate())) {
            final Bounds bounds = engine.entities().forcedFetch(WORLD).get(TransformComponent.class).bounds;

            final Grid grid = new Grid(bounds, gridSize);
            for (final Entity blocking : engine.entities().fetchAll(BLOCKING)) {
                grid.blockArea(blocking.get(TransformComponent.class).bounds);
            }
            engine.physics().setGrid(grid);
        }
    }
}
