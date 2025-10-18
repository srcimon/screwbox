package dev.screwbox.core.navigation.internal;

import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.navigation.Graph;
import dev.screwbox.core.navigation.Grid;

import java.util.List;

public class GridGraph implements Graph<Offset> {

    private final Grid grid;
    private List<Offset>[][] adjacentNodes;

    public GridGraph(final Grid grid, final boolean isDiagonalMovementAllowed) {
        this.grid = grid;
        adjacentNodes = new List[grid.width()][grid.height()];
        for (final var node : grid.nodes()) {
            adjacentNodes[node.x()][node.y()] = isDiagonalMovementAllowed
                    ? grid.freeSurroundingNodes(node)
                    : grid.freeAdjacentNodes(node);
        }
    }

    @Override
    public List<Offset> adjacentNodes(final Offset node) {
        return adjacentNodes[node.x()][node.y()];
    }

    @Override
    public double traversalCost(final Offset start, final Offset end) {
        return start.distanceTo(end);
    }

    @Override
    public Offset toGraph(final Vector position) {
        return grid.toGrid(position);
    }

    @Override
    public Vector toPosition(final Offset node) {
        return grid.toWorld(node);
    }

    @Override
    public boolean nodeExists(final Offset node) {
        return grid.isFree(node);
    }
}
