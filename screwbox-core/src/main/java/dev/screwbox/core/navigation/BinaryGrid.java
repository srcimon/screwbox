package dev.screwbox.core.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Grid;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Offset;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Stores binary data within cells that are aligned to the game world.
 */
public class BinaryGrid extends Grid<Boolean> {

    private final BitSet isBlocked;

    public BinaryGrid(final Bounds bounds, final int cellSize) {
        super(bounds, cellSize);
        isBlocked = new BitSet(width * height);
    }

    public boolean isFree(final Offset node) {
        final int x = node.x();
        final int y = node.y();
        return isInGrid(x, y) && !isBlocked.get(bitsetIndex(x, y));
    }

    public void freeAt(final Vector position) {
        statusChangeAt(position, false);
    }

    public void blockAt(final Vector position) {
        statusChangeAt(position, true);
    }

    public void block(final Offset node) {
        block(node.x(), node.y());
    }

    public void block(final int x, final int y) {
        statusChange(x, y, true);
    }

    private void statusChange(final int x, final int y, final boolean status) {
        if (isInGrid(x, y)) {
            isBlocked.set(bitsetIndex(x, y), status);
        }
    }

    private void statusChangeAt(final Vector position, boolean status) {
        final Offset node = toCell(position);
        statusChange(node.x(), node.y(), status);
    }

    public void blockArea(final Bounds area) {
        markRegion(area, true);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
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

    public int nodeCount() {
        return width * height;
    }

    public boolean isBlocked(final int x, final int y) {
        return isInGrid(x, y) && isBlocked.get(bitsetIndex(x, y));
    }

    public int cellSize() {
        return cellSize;
    }

    public boolean isBlocked(final Offset node) {
        return isBlocked(node.x(), node.y());
    }


    private int bitsetIndex(final int x, final int y) {
        return x * height + y;
    }

    private boolean isInGrid(final Offset node) {
        return node.x() > -1 && node.x() < width && node.y() > -1 && node.y() < height;
    }

    private void markRegion(final Bounds region, final boolean status) {
        final var areaTranslated = region.moveBy(-this.bounds.origin().x(), -this.bounds.origin().y()).expand(-0.1);
        final int minX = Math.max(toCell(areaTranslated.origin().x()), 0);
        final int maxX = Math.min(toCell(areaTranslated.bottomRight().x()), width - 1);
        final int minY = Math.max(toCell(areaTranslated.origin().y()), 0);
        final int maxY = Math.min(toCell(areaTranslated.bottomRight().y()), height - 1);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                isBlocked.set(bitsetIndex(x, y), status);
            }
        }
    }

    private boolean isInGrid(final int x, final int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}