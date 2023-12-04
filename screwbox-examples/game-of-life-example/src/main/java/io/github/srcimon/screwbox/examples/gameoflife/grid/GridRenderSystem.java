package io.github.srcimon.screwbox.examples.gameoflife.grid;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.World;

public class GridRenderSystem implements EntitySystem {

    private static final Archetype GRID_HOLDER = Archetype.of(GridComponent.class);

    @Override
    public void update(final Engine engine) {
        final var gridComponent = engine.environment().forcedFetch(GRID_HOLDER).get(GridComponent.class);
        final World world = engine.graphics().world();
        Bounds visibleArea = engine.graphics().world().visibleArea();
        for (final var node : gridComponent.grid.nodes()) {
            if (gridComponent.grid.isBlocked(node)) {
                final Bounds worldBounds = gridComponent.grid.worldArea(node);
                if (visibleArea.intersects(worldBounds)) {
                    final int neighbors = gridComponent.grid.blockedNeighbors(node).size();
                    final var color = colorByCountOf(neighbors, gridComponent);
                    world.fillCircle(worldBounds.position(), (int) worldBounds.width(), color);
                }
            }
        }
        final Vector snappedMousePosition = gridComponent.grid.snap(engine.mouse().position());
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
