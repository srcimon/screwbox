package dev.screwbox.gameoflife.grid;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.World;

import static dev.screwbox.core.graphics.options.OvalDrawOptions.filled;
import static dev.screwbox.core.graphics.Color.YELLOW;

public class GridRenderSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        final var gridComponent = engine.environment().fetchSingletonComponent(GridComponent.class);
        final World world = engine.graphics().world();
        Bounds visibleArea = engine.graphics().visibleArea();
        for (final var node : gridComponent.grid.nodes()) {
            if (gridComponent.grid.isBlocked(node)) {
                final Bounds worldBounds = gridComponent.grid.worldArea(node);
                if (visibleArea.intersects(worldBounds)) {
                    final int neighbors = gridComponent.grid.blockedSurroundingNodes(node).size();
                    final var color = colorByCountOf(neighbors, gridComponent);
                    world.drawCircle(worldBounds.position(), (int) worldBounds.width() / 2.0, filled(color));
                }
            }
        }
        final Vector snappedMousePosition = engine.mouse().position().snap(gridComponent.grid.cellSize());
        world.drawCircle(snappedMousePosition, 1, filled(YELLOW));
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
