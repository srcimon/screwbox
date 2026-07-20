package dev.screwbox.gameoflife.grid;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.navigation.Grid;
import dev.screwbox.core.utils.PerlinNoise;

import java.util.ArrayList;
import java.util.List;

import static dev.screwbox.core.graphics.Color.YELLOW;
import static dev.screwbox.core.graphics.options.OvalDrawOptions.filled;

public class GridRenderSystem implements EntitySystem {

    @Override
    public void update(final Engine engine) {
        final var gridComponent = engine.environment().fetchSingletonComponent(GridComponent.class);
        final World world = engine.graphics().world();
        Bounds visibleArea = engine.graphics().visibleArea();
        final Grid<Boolean> grid = gridComponent.grid;
        double z = engine.loop().runningTime().milliseconds() / 1500.0;
        for (final var node : blockedNodesIn(grid, visibleArea)) {
            final Bounds bounds = grid.cellBounds(node);
            final var r = Percent.of((PerlinNoise.generatePerlinNoise3d(123120L, node.x() / 10.0, node.y() / 10.0, z) + 1) / 2.0);
            final var g = Percent.of((PerlinNoise.generatePerlinNoise3d(14545L, node.x() / 10.0, node.y() / 10.0, z) + 1) / 2.0);
            final var b = Percent.of((PerlinNoise.generatePerlinNoise3d(53545L, node.x() / 10.0, node.y() / 10.0, z) + 1) / 2.0);
            world.drawCircle(bounds.position(), (int) bounds.width() / 2.0, filled(Color.rgb(r.rangeValue(0, 255), g.rangeValue(0, 255), b.rangeValue(0, 255))));
        }
        final Offset node = grid.toCell(engine.mouse().position());
        final Vector snappedMousePosition = grid.cellPosition(node);
        world.drawCircle(snappedMousePosition, 1, filled(YELLOW));
    }

    private List<Offset> blockedNodesIn(final Grid<Boolean> grid, final Bounds bounds) {
        final var nodes = new ArrayList<Offset>();
        final var minNode = grid.toCell(bounds.origin());
        final var maxNode = grid.toCell(bounds.bottomRight());
        final int minX = Math.max(minNode.x(), 0);
        final int maxX = Math.min(maxNode.x(), grid.size().width());
        final int minY = Math.max(minNode.y(), 0);
        final int maxY = Math.min(maxNode.y(), grid.size().height());
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                Offset offset = Offset.at(x, y);
                if (grid.hasValue(offset)) {
                    nodes.add(offset);
                }
            }
        }
        return nodes;
    }

}
