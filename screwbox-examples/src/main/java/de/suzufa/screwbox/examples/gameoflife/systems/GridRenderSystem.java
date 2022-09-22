package de.suzufa.screwbox.examples.gameoflife.systems;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.examples.gameoflife.components.GridComponent;

public class GridRenderSystem implements EntitySystem {

    private static final Archetype GRID_HOLDER = Archetype.of(GridComponent.class);

    @Override
    public void update(Engine engine) {
        var gridComponent = engine.entities().forcedFetch(GRID_HOLDER).get(GridComponent.class);

        for (var node : gridComponent.grid.nodes()) {
            Bounds worldBounds = gridComponent.grid.toWorldBounds(node);
            if (!gridComponent.grid.isFree(node)) {
                int count = gridComponent.grid.blockedNeighbors(node).size();
                engine.graphics().world().drawRectangle(worldBounds, count > 2 ? Color.RED : Color.WHITE);
            }
        }

    }

}
