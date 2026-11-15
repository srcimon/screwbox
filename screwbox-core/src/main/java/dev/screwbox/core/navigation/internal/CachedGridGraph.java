package dev.screwbox.core.navigation.internal;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.navigation.Grid;

import java.util.List;

public class CachedGridGraph extends GridGraph {

    private final List<Offset>[] adjacentNodes;
    private final int width;

    public CachedGridGraph(final Grid<Boolean> grid, final boolean isDiagonalMovementAllowed, final int detourCellCount) {
        super(grid, isDiagonalMovementAllowed, detourCellCount);
        width = grid.size().width();
        adjacentNodes = new List[grid.size().width() * grid.size().height()];
        int height = grid.size().height();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int index = y * width + x;
                adjacentNodes[index] = super.adjacentNodes(Offset.at(x, y));
            }
        }
    }

    @Override
    public List<Offset> adjacentNodes(final Offset node) {
        return adjacentNodes[node.x() + node.y() * width];
    }

}
