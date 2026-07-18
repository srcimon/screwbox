package dev.screwbox.core.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.graphics.Offset;

import java.util.ArrayList;
import java.util.List;

//TODO get rid completly!

/**
 * Stores binary data within cells that are aligned to the game world.
 */
public class BinaryGrid extends Grid<Boolean> {

    public BinaryGrid(final Bounds bounds, final int cellSize) {
        super(bounds, cellSize);
    }

    public List<Offset> freeNodes(final Offset node, final boolean isDiagonalMovementAllowed) {
        final List<Offset> outNeighbors = new ArrayList<>();
        final Offset bottom = node.bottom();
        final Offset top    = node.top();
        final Offset left   = node.left();
        final Offset right  = node.right();

        final boolean isBottomFree = isFree(bottom);
        final boolean isTopFree    = isFree(top);
        final boolean isLeftFree   = isFree(left);
        final boolean isRightFree  = isFree(right);

        if (isBottomFree) outNeighbors.add(bottom);
        if (isTopFree)    outNeighbors.add(top);
        if (isLeftFree)   outNeighbors.add(left);
        if (isRightFree)  outNeighbors.add(right);

        if (isDiagonalMovementAllowed) {
            if (isBottomFree && isRightFree) {
                final Offset diag = node.bottomRight();
                if (isFree(diag)) outNeighbors.add(diag);
            }
            if (isBottomFree && isLeftFree) {
                final Offset diag = node.bottomLeft();
                if (isFree(diag)) outNeighbors.add(diag);
            }
            if (isTopFree && isLeftFree) {
                final Offset diag = node.topLeft();
                if (isFree(diag)) outNeighbors.add(diag);
            }
            if (isTopFree && isRightFree) {
                final Offset diag = node.topRight();
                if (isFree(diag)) outNeighbors.add(diag);
            }
        }
        return outNeighbors;
    }

    private boolean isFree(final Offset node) {
        return contains(node) && !hasValue(node);
    }

}