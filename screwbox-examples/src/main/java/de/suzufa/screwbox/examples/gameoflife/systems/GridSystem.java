package de.suzufa.screwbox.examples.gameoflife.systems;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.physics.Grid;
import de.suzufa.screwbox.core.physics.Grid.Node;
import de.suzufa.screwbox.core.utils.ListUtil;

public class GridSystem implements EntitySystem {

    // TODO: render and update system / entity that holds grid
    private Grid grid = createInitialGrid();

    @Override
    public void update(Engine engine) {
        Grid newGrid = new Grid(Bounds.$$(0, 0, 200, 200), 16);// TODO: clone?

        for (Node node2 : grid.nodes()) {
            int count = 8 - grid.freeNeighbors(node2).size();
            if (grid.isFree(node2)) {
                if (count == 2 || count == 3) {
                    newGrid.block(node2);
                }
            } else {
                if (count == 3) {
                    newGrid.block(node2);
                }
            }
        }
        for (var node : grid.nodes()) {
            Bounds worldBounds = grid.toWorldBounds(node);
            engine.graphics().world().drawRectangle(worldBounds, grid.isFree(node) ? Color.GREEN : Color.BLUE);
        }
        grid = newGrid;
    }

    private Grid createInitialGrid() {
        Grid grid = new Grid(Bounds.$$(0, 0, 200, 200), 16);
        for (int i = 0; i < grid.nodes().size() / 2.5; i++) {
            grid.block(ListUtil.randomFrom(grid.nodes()));
        }
        return grid;
    }

}
