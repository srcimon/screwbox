package de.suzufa.screwbox.examples.gameoflife.systems;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.World;
import de.suzufa.screwbox.examples.gameoflife.components.GridComponent;

public class GridRenderSystem implements EntitySystem {

    private static final Archetype GRID_HOLDER = Archetype.of(GridComponent.class);
    int i = 0;

    @Override
    public void update(final Engine engine) {
        final var grid = engine.entities().forcedFetch(GRID_HOLDER).get(GridComponent.class).grid;
        final World world = engine.graphics().world();
        for (final var node : grid.nodes()) {
            if (grid.isBlocked(node)) {
                final int neighbors = grid.blockedNeighbors(node).size();
                final var color = colorByCountOf(neighbors);
                final Bounds worldBounds = grid.toWorldBounds(node);
                world.drawCircle(worldBounds.position(), (int) worldBounds.width(), color);
            }
        }

        final Vector snappedMousePosition = grid.snap(engine.mouse().worldPosition());
        world.drawCircle(snappedMousePosition, 2, Color.YELLOW);
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
