package dev.screwbox.core.navigation.internal;

import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.navigation.Graph;
import dev.screwbox.core.navigation.Grid;

import java.util.List;

public class GridGraph implements Graph<Offset> {

    private final Grid grid;
    private boolean isDiagonalMovementAllowed;

    public GridGraph(final Grid grid, final boolean isDiagonalMovementAllowed) {
        this.grid = grid;
        this.isDiagonalMovementAllowed = isDiagonalMovementAllowed;
    }

    @Override
    public List<Offset> adjacentNodes(final Offset node) {
        return isDiagonalMovementAllowed
                ? grid.freeSurroundingNodes(node)
                : grid.freeAdjacentNodes(node);
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
