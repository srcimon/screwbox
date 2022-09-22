package de.suzufa.screwbox.examples.gameoflife.systems;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.physics.Grid;
import de.suzufa.screwbox.core.physics.Grid.Node;
import de.suzufa.screwbox.core.utils.ListUtil;
import de.suzufa.screwbox.core.utils.Timer;

public class GridSystem implements EntitySystem {

    // TODO: render and update system / entity that holds grid
    private Grid grid = createInitialGrid();
    private Timer timer = Timer.withInterval(Duration.ofMillis(100));

    @Override
    public void update(Engine engine) {
        if (timer.isTick()) {
            Grid newGrid = grid.cleared();

            for (Node node2 : grid.nodes()) {
                int count = grid.blockedNeighbors(node2).size();
                if (grid.isFree(node2)) {
                    if (count == 3) {
                        newGrid.block(node2);
                    }
                } else if (count == 2 || count == 3) {
                    newGrid.block(node2);
                }
            }
            grid = newGrid;
        }
        for (var node : grid.nodes()) {
            Bounds worldBounds = grid.toWorldBounds(node);
            if (!grid.isFree(node)) {
                int count = grid.blockedNeighbors(node).size();
                engine.graphics().world().drawRectangle(worldBounds, count > 2 ? Color.RED : Color.WHITE);
            }
        }
    }

    private Grid createInitialGrid() {
        Grid grid = new Grid(Bounds.$$(-240, -240, 600, 600), 8);
        for (int i = 0; i < 500; i++) {
            grid.block(ListUtil.randomFrom(grid.nodes()));
        }
        return grid;
    }

}
