package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.World;

public class Raster {

    private final boolean[][] isBlocked;
    private final Vector origin;
    private final int width;
    private final int height;
    private final int gridSize;

    public Raster(Bounds bounds, int gridSize) {
        width = (int) bounds.width() / gridSize;
        height = (int) bounds.height() / gridSize;
        origin = bounds.origin();
        this.gridSize = gridSize;
        isBlocked = new boolean[width][height];
    }

    public int[][] createMap() {
        int[][] map = new int[height][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[y][x] = isBlocked[x][y] ? 1 : 0;
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

    public void debugDraw(World world) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = isBlocked[x][y] ? Color.RED : Color.GREEN;
                Bounds bounds = Bounds.atOrigin(
                        origin.x() + x * gridSize,
                        origin.y() + y * gridSize,
                        gridSize,
                        gridSize);
                world.drawRectangle(bounds, color.withOpacity(0.5));
            }
        }
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
