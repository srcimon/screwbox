package de.suzufa.screwbox.examples.gameoflife.systems;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.physics.Grid;
import de.suzufa.screwbox.core.utils.ListUtil;

public class GridSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        Grid grid = new Grid(Bounds.$$(0, 0, 200, 200), 16);
        grid.block(ListUtil.randomFrom(grid.nodes()));

        for (var node : grid.nodes()) {
            Bounds worldBounds = grid.toWorldBounds(node);
            engine.graphics().world().drawRectangle(worldBounds, grid.isFree(node) ? Color.GREEN : Color.BLUE);
        }
    }

}
