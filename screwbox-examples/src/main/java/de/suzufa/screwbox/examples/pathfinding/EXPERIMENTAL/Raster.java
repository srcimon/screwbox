package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;

public class Raster {

    private final boolean[][] isBlocked;
    private final int width;
    private final int height;
    private final int gridSize;

    public Raster(final Bounds bounds, final int gridSize) {
        width = (int) bounds.width() / gridSize;
        height = (int) bounds.height() / gridSize;
        this.gridSize = gridSize;
        isBlocked = new boolean[width][height];
    }

    public boolean isFree(final RasterPoint point) {
        if (point.y < 0 || point.y > isBlocked[0].length - 1) {
            return false;
        }
        if (point.x < 0 || point.x > isBlocked.length - 1) {
            return false;
        }
        return !isBlocked[point.x][point.y];
    }

    @Deprecated
    public boolean isBlocked(final int x, final int y) {
        // TODO: outOfRangeCheck
        return isBlocked[x][y];
    }

    public Vector getVector(final RasterPoint point) {
        return Vector.of((point.x + 0.5) * gridSize, (point.y + 0.5) * gridSize);
    }

    public RasterPoint getPoint(final Vector position) {
        return new RasterPoint((int) position.x() / gridSize, (int) position.y() / gridSize, null);
    }

    public void blockArea(final Bounds bounds) {
        final Vector boundsOrigin = bounds.origin();
        final int xMin = (int) boundsOrigin.x() / gridSize;
        final int yMin = (int) boundsOrigin.y() / gridSize;

        final Vector bottomRight = bounds.bottomRight();
        final int xMax = (int) bottomRight.x() / gridSize;
        final int yMax = (int) bottomRight.y() / gridSize;
        for (int x = xMin; x < xMax; x++) {
            for (int y = yMin; y < yMax; y++) {
                isBlocked[x][y] = true;
            }
        }
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    // TODO: Sub Algorithm wihtout diagonal movements
    public List<RasterPoint> findNeighbors(final RasterPoint point) {
        final List<RasterPoint> neighbors = new ArrayList<>();
        final RasterPoint downLeft = point.offset(-1, 1);
        final RasterPoint downRight = point.offset(1, 1);
        final RasterPoint upLeft = point.offset(-1, -1);
        final RasterPoint upRight = point.offset(1, -1);
        final RasterPoint down = point.offset(0, 1);
        final RasterPoint up = point.offset(0, -1);
        final RasterPoint left = point.offset(-1, 0);
        final RasterPoint right = point.offset(1, 0);
        if (isFree(down))
            neighbors.add(down);
        if (isFree(up))
            neighbors.add(up);
        if (isFree(left))
            neighbors.add(left);
        if (isFree(right))
            neighbors.add(right);
        if (isFree(downRight) && isFree(down) && isFree(right))
            neighbors.add(downRight);
        if (isFree(downLeft) && isFree(down) && isFree(left))
            neighbors.add(downLeft);
        if (isFree(upLeft) && isFree(up) && isFree(left))
            neighbors.add(upLeft);
        if (isFree(upRight) && isFree(up) && isFree(right))
            neighbors.add(upRight);
        return neighbors;
    }
}
