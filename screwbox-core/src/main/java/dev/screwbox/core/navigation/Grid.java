package dev.screwbox.core.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.utils.Validate;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Stores binary data within cells that are aligned to the game world.
 */
public class Grid implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final BitSet isBlocked;
    private final int width;
    private final int height;
    private final int cellSize;
    private final Bounds bounds;

    /**
     * Returns the cell that this {@link Vector} would reside in within a grid with the specified cell size.
     *
     * @param cellSize size of cell (must be positive)
     * @since 3.12.0
     */
    public static Offset findCell(final Vector vector, final int cellSize) {
        Validate.positive(cellSize, "cell size must be positive");
        return Offset.at(Math.floorDiv((int) vector.x(), cellSize), Math.floorDiv((int) vector.y(), cellSize));
    }

    public Grid(final Bounds bounds, final int cellSize) {
        requireNonNull(bounds, "grid bounds must not be null");
        Validate.positive(cellSize, "cell size must be positive");
        Validate.isTrue(() -> bounds.origin().x() % cellSize == 0, "bounds should fit cell size");
        Validate.isTrue(() -> bounds.origin().y() % cellSize == 0, "bounds should fit cell size");

        this.cellSize = cellSize;
        this.bounds = bounds;
        width = toGrid(bounds.width());
        height = toGrid(bounds.height());
        isBlocked = new BitSet(width * height);
    }

    /**
     * Returns the area of this {@link Grid} in the {@link World}.
     */
    public Bounds bounds() {
        return bounds;
    }

    /**
     * Returns {@code true} if the specified position is not blocked and inside the {@link Grid}.
     */
    public boolean isFree(final int x, final int y) {
        return isInGrid(x, y) r&& !isBlocked.get(bitsetIndex(x, y));
    }

    public boolean isFree(final Offset node) {
        return isFree(node.x(), node.y());
    }

    public Vector toWorld(final Offset node) {
        final double x = (node.x() + 0.5) * cellSize + bounds.origin().x();
        final double y = (node.y() + 0.5) * cellSize + bounds.origin().y();
        return Vector.$(x, y);
    }

    public Bounds nodeBounds(final Offset node) {
        final Vector position = toWorld(node);
        return Bounds.atPosition(position, cellSize, cellSize);
    }

    public Offset toGrid(final Vector position) {
        final var translated = position.subtract(bounds.origin());
        return Grid.findCell(translated, cellSize);
    }

    public void freeArea(final Bounds area) {
        markRegion(area, false);
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
        final Offset node = toGrid(position);
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

    public List<Offset> nodes() {
        final var nodes = new ArrayList<Offset>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                nodes.add(Offset.at(x, y));
            }
        }
        return nodes;
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

    private int toGrid(final double value) {
        return Math.floorDiv((int) value, cellSize);
    }

    private int bitsetIndex(final int x, final int y) {
        return x * height + y;
    }

    private boolean isInGrid(final Offset node) {
        return node.x() > -1 && node.x() < width && node.y() > -1 && node.y() < height;
    }

    private void markRegion(final Bounds region, final boolean status) {
        final var areaTranslated = region.moveBy(-this.bounds.origin().x(), -this.bounds.origin().y()).expand(-0.1);
        final int minX = Math.max(toGrid(areaTranslated.origin().x()), 0);
        final int maxX = Math.min(toGrid(areaTranslated.bottomRight().x()), width - 1);
        final int minY = Math.max(toGrid(areaTranslated.origin().y()), 0);
        final int maxY = Math.min(toGrid(areaTranslated.bottomRight().y()), height - 1);
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