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
        final List<Offset> outNeighbors = new ArrayList<>(isDiagonalMovementAllowed ? 8 : 4);

        final Offset bottom = node.bottom();
        final boolean isBottomFree = isFree(bottom);
        if (isBottomFree) {
            outNeighbors.add(bottom);
        }

        final Offset top = node.top();
        final boolean isTopFree = isFree(top);
        if (isTopFree) {
            outNeighbors.add(top);
        }

        final Offset left = node.left();
        final boolean isLeftFree = isFree(left);
        if (isLeftFree) {
            outNeighbors.add(left);
        }

        final Offset right = node.right();
        final boolean isRightFree = isFree(right);
        if (isRightFree) {
            outNeighbors.add(right);
        }

        if (isDiagonalMovementAllowed) {
            if (isBottomFree && isRightFree) {
                final Offset diag = node.bottomRight();
                if (isFree(diag)) {
                    outNeighbors.add(diag);
                }
            }
            if (isBottomFree && isLeftFree) {
                final Offset diag = node.bottomLeft();
                if (isFree(diag)) {
                    outNeighbors.add(diag);
                }
            }
            if (isTopFree && isLeftFree) {
                final Offset diag = node.topLeft();
                if (isFree(diag)) {
                    outNeighbors.add(diag);
                }
            }
            if (isTopFree && isRightFree) {
                final Offset diag = node.topRight();
                if (isFree(diag)) {
                    outNeighbors.add(diag);
                }
            }
        }
        return outNeighbors;
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
        return grid.toWorld(node);
    }

    @Override
    public boolean nodeExists(final Offset node) {
        return grid.contains(node);
    }

    private boolean isFree(final Offset node) {
        return grid.contains(node) && !grid.hasValue(node);
    }

}
