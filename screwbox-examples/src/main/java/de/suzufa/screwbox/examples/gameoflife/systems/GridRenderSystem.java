package de.suzufa.screwbox.examples.gameoflife.systems;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Grid;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.World;
import de.suzufa.screwbox.examples.gameoflife.components.GridComponent;

public class GridRenderSystem implements EntitySystem {

    private static final Archetype GRID_HOLDER = Archetype.of(GridComponent.class);

    @Override
    public void update(final Engine engine) {
        final var gridComponent = engine.entities().forcedFetch(GRID_HOLDER).get(GridComponent.class);
        final World world = engine.graphics().world();
        Grid grid = gridComponent.grid;
        for (final var node : grid.nodes()) {// TODO: ONLY NODES ON SCREEN
            final Bounds worldBounds = grid.toWorldBounds(node);
            if (!grid.isFree(node)) {
                int neighbors = grid.blockedNeighbors(node).size();
                final var color = neighbors > 2
                        ? neighbors > 3 ? Color.BLUE : Color.RED
                        : Color.WHITE;
                world.drawCircle(worldBounds.position(), (int) worldBounds.width(), color);
            }
        }

        Vector snappedMousePosition = grid.snap(engine.mouse().worldPosition());
        world.drawCircle(snappedMousePosition, 2, Color.YELLOW);
    }

}
