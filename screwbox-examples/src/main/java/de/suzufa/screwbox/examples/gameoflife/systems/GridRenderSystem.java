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
        Vector snappedMousePosition = grid.snap(engine.mouse().worldPosition());

        world.drawCircle(snappedMousePosition, 2, Color.YELLOW);
        for (final var node : grid.nodes()) {
            final Bounds worldBounds = grid.toWorldBounds(node);
            if (!grid.isFree(node)) {
                final var color = grid.blockedNeighbors(node).size() > 2 ? Color.RED : Color.WHITE;
                world.drawCircle(worldBounds.position(), (int) worldBounds.width(), color);
            }
        }

    }

}
