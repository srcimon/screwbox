package io.github.simonbas.screwbox.examples.gameoflife.systems;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Grid;
import io.github.simonbas.screwbox.core.Grid.Node;
import io.github.simonbas.screwbox.core.entities.Archetype;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.utils.Timer;
import io.github.simonbas.screwbox.examples.gameoflife.components.GridComponent;

import static io.github.simonbas.screwbox.core.Duration.ofMillis;

public class GridUpdateSystem implements EntitySystem {

    private static final Archetype GRID_HOLDER = Archetype.of(GridComponent.class);
    private static final Timer TIMER = Timer.withInterval(ofMillis(100));

    @Override
    public void update(final Engine engine) {
        final var gridComponent = engine.entities().forcedFetch(GRID_HOLDER).get(GridComponent.class);

        if (!engine.async().hasActiveTasks(gridComponent) && TIMER.isTick()) {
            engine.async().run(gridComponent, () -> update(gridComponent));
        }
    }

    private void update(final GridComponent gridComponent) {
        final Grid oldGrid = gridComponent.grid;
        final Grid grid = oldGrid.clearedInstance();
        for (final Node node : oldGrid.nodes()) {
            final int count = oldGrid.blockedNeighbors(node).size();
            if (oldGrid.isFree(node)) {
                if (count == 3) {
                    grid.block(node);
                }
            } else if (count == 2 || count == 3) {
                grid.block(node);
            }
        }
        gridComponent.grid = grid;
    }

}
