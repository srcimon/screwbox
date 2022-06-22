package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;

public class Raster {

    private final boolean[][] isBlocked;
    private final int width;
    private final int height;
    private final int gridSize;

    public Raster(Bounds bounds, int gridSize) {
        width = (int) bounds.width() / gridSize;
        height = (int) bounds.height() / gridSize;
        this.gridSize = gridSize;
        isBlocked = new boolean[width][height];
    }

    public int[][] createMap() {
        int[][] map = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = isBlocked[x][y] ? 1 : 0;
            }
        }
        return map;
    }

    public Vector getVector(RasterPoint point) {
        return Vector.of((point.x + 0.5) * gridSize, (point.y + 0.5) * gridSize);
    }

    public RasterPoint getPoint(Vector position) {
        return new RasterPoint((int) position.x() / gridSize, (int) position.y() / gridSize, null);
    }

    public void blockArea(Bounds bounds) {
        Vector boundsOrigin = bounds.origin();
        int xMin = (int) boundsOrigin.x() / gridSize;
        int yMin = (int) boundsOrigin.y() / gridSize;

        Vector bottomRight = bounds.bottomRight();
        int xMax = (int) bottomRight.x() / gridSize;
        int yMax = (int) bottomRight.y() / gridSize;
        for (int x = xMin; x < xMax; x++) {
            for (int y = yMin; y < yMax; y++) {
                isBlocked[x][y] = true;
            }
        }
    }

}
