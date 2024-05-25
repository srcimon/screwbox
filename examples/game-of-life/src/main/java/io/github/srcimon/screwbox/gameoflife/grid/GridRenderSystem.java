package io.github.srcimon.screwbox.gameoflife.grid;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.World;

import static io.github.srcimon.screwbox.core.graphics.CircleDrawOptions.filled;
import static io.github.srcimon.screwbox.core.graphics.Color.YELLOW;

public class GridRenderSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        final var gridComponent = engine.environment().fetchSingletonComponent(GridComponent.class);
        final World world = engine.graphics().world();
        Bounds visibleArea = engine.graphics().world().visibleArea();
        for (final var node : gridComponent.grid.nodes()) {
            if (gridComponent.grid.isBlocked(node)) {
                final Bounds worldBounds = gridComponent.grid.worldArea(node);
                if (visibleArea.intersects(worldBounds)) {
                    final int neighbors = gridComponent.grid.blockedNeighbors(node).size();
                    final var color = colorByCountOf(neighbors, gridComponent);
                    world.drawCircle(worldBounds.position(), (int) worldBounds.width() / 2.0, filled(color));
                }
            }
        }
        final Vector snappedMousePosition = gridComponent.grid.snap(engine.mouse().position());
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
