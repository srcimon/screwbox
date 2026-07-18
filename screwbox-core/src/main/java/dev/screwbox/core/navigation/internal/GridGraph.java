package dev.screwbox.core.navigation.internal;

import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.navigation.Graph;
import dev.screwbox.core.navigation.Grid;

import java.util.ArrayList;
import java.util.List;

public class GridGraph implements Graph<Offset> {

    private final Grid<Boolean> grid;
    private final boolean isDiagonalMovementAllowed;

    public GridGraph(final Grid<Boolean> grid, final boolean isDiagonalMovementAllowed) {
        this.grid = grid;
        this.isDiagonalMovementAllowed = isDiagonalMovementAllowed;
    }

    @Override
    public List<Offset> adjacentNodes(final Offset node) {
        final List<Offset> nodes = new ArrayList<>(isDiagonalMovementAllowed ? 8 : 4);

        addIfFree(node.bottom(), nodes);
        addIfFree(node.top(), nodes);
        addIfFree(node.left(), nodes);
        addIfFree(node.right(), nodes);

        if (isDiagonalMovementAllowed) {
            final boolean isBottomFree = isFree(node.bottom());
            final boolean isLeftFree = isFree(node.left());
            final boolean isRightFree = isFree(node.right());
            if (isBottomFree && isRightFree) {
                addIfFree(node.bottomRight(), nodes);
            }
            if (isBottomFree && isLeftFree) {
                addIfFree(node.bottomLeft(), nodes);
            }
            final boolean isTopFree = isFree(node.top());
            if (isTopFree && isLeftFree) {
                addIfFree(node.topLeft(), nodes);
            }
            if (isTopFree && isRightFree) {
                addIfFree(node.topRight(), nodes);
            }
        }
        return nodes;
    }

    @Override
    public double traversalCost(final Offset start, final Offset end) {
        return start.distanceTo(end);
    }

    @Override
    public Offset toGraph(final Vector position) {
        return grid.toCell(position);
    }

    @Override
    public Vector toPosition(final Offset node) {
        return grid.cellPosition(node);
    }

    @Override
    public boolean nodeExists(final Offset node) {
        return grid.contains(node);
    }

    private boolean isFree(final Offset node) {
        return grid.contains(node) && !grid.hasValue(node);
    }

    private void addIfFree(final Offset node, final List<Offset> offsets) {
        if (isFree(node)) {
            offsets.add(node);
        }
    }
}
