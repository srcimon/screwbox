package de.suzufa.screwbox.examples.gameoflife.systems;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Grid;
import de.suzufa.screwbox.core.Grid.Node;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.utils.Timer;
import de.suzufa.screwbox.examples.gameoflife.components.GridComponent;

public class GridUpdateSystem implements EntitySystem {

    private static final Archetype GRID_HOLDER = Archetype.of(GridComponent.class);
    private final Timer timer = Timer.withInterval(Duration.ofMillis(80));

    @Override
    public void update(final Engine engine) {

        if (timer.isTick()) {
            final var gridComponent = engine.entities().forcedFetch(GRID_HOLDER).get(GridComponent.class);
            final Grid newGrid = gridComponent.grid.cleared();

            for (final Node node2 : gridComponent.grid.nodes()) {
                final int count = gridComponent.grid.blockedNeighbors(node2).size();
                if (gridComponent.grid.isFree(node2)) {
                    if (count == 3) {
                        newGrid.block(node2);
                    }
                } else if (count == 2 || count == 3) {
                    newGrid.block(node2);
                }
            }
            gridComponent.grid = newGrid;
        }
    }

}
