package io.github.srcimon.screwbox.examples.gameoflife.grid;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.World;
import io.github.srcimon.screwbox.examples.gameoflife.grid.GridComponent;

public class GridRenderSystem implements EntitySystem {

    private static final Archetype GRID_HOLDER = Archetype.of(GridComponent.class);

    @Override
    public void update(final Engine engine) {
        final var gridComponent = engine.entities().forcedFetch(GRID_HOLDER).get(GridComponent.class);
        final World world = engine.graphics().world();
        for (final var node : gridComponent.grid.nodes()) {
            if (gridComponent.grid.isBlocked(node)) {
                final int neighbors = gridComponent.grid.blockedNeighbors(node).size();
                final var color = colorByCountOf(neighbors, gridComponent);
                final Bounds worldBounds = gridComponent.grid.worldArea(node);
                world.fillCircle(worldBounds.position(), (int) worldBounds.width(), color);
            }
        }

        final Vector snappedMousePosition = gridComponent.grid.snap(engine.mouse().worldPosition());
        world.fillCircle(snappedMousePosition, 2, Color.YELLOW);
    }

    private Color colorByCountOf(final int neighbors, final GridComponent gridComponent) {
        if (neighbors == 1) {
            return gridComponent.oneNeighboursColor;
        }
        if (neighbors == 2) {
            return gridComponent.twoNeighboursColor;
        }
        return gridComponent.noNeighboursColor;
    }

}
