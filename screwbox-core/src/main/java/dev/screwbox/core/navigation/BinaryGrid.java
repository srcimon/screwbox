package dev.screwbox.core.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
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

    public boolean isFree(final Offset node) {
        final int x = node.x();
        final int y = node.y();
        return isInGrid(x, y) && !hasValue(node);
    }

    public void block(final Offset node) {
        block(node.x(), node.y());
    }

    public void block(final int x, final int y) {
        set(x, y, true);
    }

    public void blockArea(final Bounds area) {
        set(area, true);
    }

    //TODO move into GridGraph?
    public List<Offset> freeAdjacentNodes(final Offset node) {
        final List<Offset> neighbors = new ArrayList<>(4);

        final Offset bottom = node.bottom();
        if (isFree(bottom)) {
            neighbors.add(bottom);
        }

        final Offset top = node.top();
        if (isFree(top)) {
            neighbors.add(top);
        }

        final Offset left = node.left();
        if (isFree(left)) {
            neighbors.add(left);
        }

        final Offset right = node.right();
        if (isFree(right)) {
            neighbors.add(right);
        }

        return neighbors;
    }

    //TODO move into GridGraph?
    public List<Offset> freeSurroundingNodes(final Offset node) {
        final List<Offset> neighbors = new ArrayList<>(8);

        final Offset bottom = node.bottom();
        final boolean isBottomFree = isFree(bottom);
        if (isBottomFree) {
            neighbors.add(bottom);
        }

        final Offset top = node.top();
        final boolean isTopFree = isFree(top);
        if (isTopFree) {
            neighbors.add(top);
        }

        final Offset left = node.left();
        final boolean isLeftFree = isFree(left);
        if (isLeftFree) {
            neighbors.add(left);
        }

        final Offset right = node.right();
        final boolean isRightFree = isFree(right);
        if (isRightFree) {
            neighbors.add(right);
        }

        if (isBottomFree) {
            if (isRightFree) {
                final Offset bottomRight = node.bottomRight();
                if (isFree(bottomRight)) {
                    neighbors.add(bottomRight);
                }
            }
            if (isLeftFree) {
                final Offset bottomLeft = node.bottomLeft();
                if (isFree(bottomLeft)) {
                    neighbors.add(bottomLeft);
                }
            }
        }

        if (isTopFree) {
            if (isLeftFree) {
                final Offset topLeft = node.topLeft();
                if (isFree(topLeft)) {
                    neighbors.add(topLeft);
                }
            }
            if (isRightFree) {
                final Offset topRight = node.topRight();
                if (isFree(topRight)) {
                    neighbors.add(topRight);
                }
            }
        }

        return neighbors;
    }

}