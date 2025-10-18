package dev.screwbox.core.navigation.internal;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.navigation.Grid;

import java.util.List;

public class CachedGridGraph extends GridGraph {

    private final List<Offset>[][] adjacentNodes;

    public CachedGridGraph(final Grid grid, final boolean isDiagonalMovementAllowed) {
        super(grid, isDiagonalMovementAllowed);
        System.out.println("XX");
        adjacentNodes = new List[grid.width()][grid.height()];
        for (final var node : grid.nodes()) {
            adjacentNodes[node.x()][node.y()] = super.adjacentNodes(node);
        }
    }

    @Override
    public List<Offset> adjacentNodes(final Offset node) {
        return adjacentNodes[node.x()][node.y()];
    }

}
