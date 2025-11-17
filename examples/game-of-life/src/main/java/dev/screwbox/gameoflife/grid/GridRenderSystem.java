package dev.screwbox.gameoflife.grid;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.navigation.Grid;

import static dev.screwbox.core.graphics.Color.YELLOW;
import static dev.screwbox.core.graphics.options.OvalDrawOptions.filled;

public class GridRenderSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        final var gridComponent = engine.environment().fetchSingletonComponent(GridComponent.class);
        final World world = engine.graphics().world();
        Bounds visibleArea = engine.graphics().visibleArea();
        final Grid grid = gridComponent.grid;
Time t = Time.now();
        for (final var node : grid.nodesIn(visibleArea)) {
            if (grid.isBlocked(node)) {
                final Bounds bounds = grid.nodeBounds(node);
                final int neighbors = grid.blockedSurroundingNodes(node).size();
                final var color = colorByCountOf(neighbors, gridComponent);
                world.drawCircle(bounds.position(), (int) bounds.width() / 2.0, filled(color));
            }
        }
        System.out.println(Duration.since(t).nanos());
        final Vector snappedMousePosition = grid.snap(engine.mouse().position());
        world.drawCircle(snappedMousePosition, 1, filled(YELLOW));
    }

    private Color colorByCountOf(final int neighbors, final GridComponent gridComponent) {
        return switch (neighbors) {
            case 1 -> gridComponent.oneNeighboursColor;
            case 2 -> gridComponent.twoNeighboursColor;
            default -> gridComponent.noNeighboursColor;
        };
    }

}
