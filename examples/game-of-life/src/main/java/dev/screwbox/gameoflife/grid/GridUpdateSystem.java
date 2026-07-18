package dev.screwbox.gameoflife.grid;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.navigation.Grid;
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
        final Grid<Boolean> oldGrid = gridComponent.grid;
        final Grid<Boolean> grid = Grid.booleanGrid(oldGrid.bounds(), oldGrid.cellSize());
        oldGrid.cells().stream().parallel().forEach(node -> {
            final int count = blockedSurroundingNodesCount(oldGrid, node);
            if (oldGrid.contains(node) && !oldGrid.hasValue(node)) {
                if (count == 3) {
                    grid.set(node, true);
                }
            } else if (count == 2 || count == 3) {
                grid.set(node, true);
            }
        });
        gridComponent.grid = grid;
    }

    private int blockedSurroundingNodesCount(final Grid<Boolean> grid, final Offset node) {
        int count = 0;
        if (grid.hasValue(node.top())) {
            count++;
        }
        if (grid.hasValue(node.topRight())) {
            count++;
        }
        if (grid.hasValue(node.right())) {
            count++;
        }
        if (grid.hasValue(node.bottomRight())) {
            count++;
        }
        if (grid.hasValue(node.bottom())) {
            count++;
        }
        if (grid.hasValue(node.bottomLeft())) {
            count++;
        }
        if (grid.hasValue(node.left())) {
            count++;
        }
        if (grid.hasValue(node.topLeft())) {
            count++;
        }
        return count;
    }
}
