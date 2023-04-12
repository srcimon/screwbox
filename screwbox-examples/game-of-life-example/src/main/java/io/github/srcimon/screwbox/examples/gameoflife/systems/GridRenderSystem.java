package io.github.srcimon.screwbox.examples.gameoflife.systems;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.World;
import io.github.srcimon.screwbox.examples.gameoflife.components.GridComponent;

public class GridRenderSystem implements EntitySystem {

    private static final Archetype GRID_HOLDER = Archetype.of(GridComponent.class);

    @Override
    public void update(final Engine engine) {
        final var grid = engine.entities().forcedFetch(GRID_HOLDER).get(GridComponent.class).grid;
        final World world = engine.graphics().world();
        for (final var node : grid.nodes()) {
            if (grid.isBlocked(node)) {
                final int neighbors = grid.blockedNeighbors(node).size();
                final var color = colorByCountOf(neighbors);
                final Bounds worldBounds = grid.worldArea(node);
                world.fillCircle(worldBounds.position(), (int) worldBounds.width(), color);
            }
        }

        final Vector snappedMousePosition = grid.snap(engine.mouse().worldPosition());
        world.fillCircle(snappedMousePosition, 2, Color.YELLOW);
    }

    private Color colorByCountOf(final int neighbors) {
        if (neighbors == 1) {
            return Color.BLUE;
        }
        if (neighbors == 2) {
            return Color.WHITE;
        }
        return Color.RED;
    }

}
