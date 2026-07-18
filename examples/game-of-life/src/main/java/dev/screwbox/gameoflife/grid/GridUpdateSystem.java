package dev.screwbox.gameoflife.grid;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.navigation.BinaryGrid;
import dev.screwbox.core.utils.Scheduler;

import static dev.screwbox.core.Duration.ofMillis;

public class GridUpdateSystem implements EntitySystem {

    private static final Scheduler SCHEDULER = Scheduler.withInterval(ofMillis(100));

    @Override
    public void update(final Engine engine) {
        final var gridComponent = engine.environment().fetchSingletonComponent(GridComponent.class);

        if (engine.async().hasNoActiveTask(gridComponent) && SCHEDULER.isTick()) {
            engine.async().run(gridComponent, () -> update(gridComponent));
        }
    }

    private void update(final GridComponent gridComponent) {
        final BinaryGrid oldGrid = gridComponent.grid;
        final BinaryGrid grid = new BinaryGrid(oldGrid.bounds(), oldGrid.cellSize());
        oldGrid.nodes().stream().parallel().forEach(node -> {
            final int count = blockedSurroundingNodesCount(oldGrid, node);
            if (oldGrid.isFree(node)) {
                if (count == 3) {
                    grid.block(node);
                }
            } else if (count == 2 || count == 3) {
                grid.block(node);
            }
        });
        gridComponent.grid = grid;
    }

    private int blockedSurroundingNodesCount(final BinaryGrid grid, final Offset node) {
        int count = 0;
        if (grid.isBlocked(node.top())) {
            count++;
        }
        if (grid.isBlocked(node.topRight())) {
            count++;
        }
        if (grid.isBlocked(node.right())) {
            count++;
        }
        if (grid.isBlocked(node.bottomRight())) {
            count++;
        }
        if (grid.isBlocked(node.bottom())) {
            count++;
        }
        if (grid.isBlocked(node.bottomLeft())) {
            count++;
        }
        if (grid.isBlocked(node.left())) {
            count++;
        }
        if (grid.isBlocked(node.topLeft())) {
            count++;
        }
        return count;
    }
}
