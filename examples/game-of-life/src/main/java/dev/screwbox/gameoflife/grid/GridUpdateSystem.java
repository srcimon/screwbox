package dev.screwbox.gameoflife.grid;

import dev.screwbox.core.Engine;
import dev.screwbox.core.physics.Grid;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.utils.Scheduler;

import static dev.screwbox.core.Duration.ofMillis;

public class GridUpdateSystem implements EntitySystem {

    private static final Scheduler SCHEDULER = Scheduler.withInterval(ofMillis(100));

    @Override
    public void update(final Engine engine) {
        final var gridComponent = engine.environment().fetchSingletonComponent(GridComponent.class);

        if (!engine.async().hasActiveTasks(gridComponent) && SCHEDULER.isTick()) {
            engine.async().run(gridComponent, () -> update(gridComponent));
        }
    }

    private void update(final GridComponent gridComponent) {
        final Grid oldGrid = gridComponent.grid;
        final Grid grid = oldGrid.clearedInstance();
        oldGrid.nodes().stream().parallel().forEach(node -> {
            final int count = oldGrid.blockedNeighbors(node).size();
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
}
