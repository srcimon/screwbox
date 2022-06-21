package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.World;

public class CollisionMap {

    private boolean[][] map;
    private Vector origin;
    private int width;
    private int height;
    private int gridSize;

    public CollisionMap(Bounds worldBounds, int gridSize) {
        width = (int) worldBounds.width() / gridSize;
        height = (int) worldBounds.height() / gridSize;
        origin = worldBounds.origin();
        this.gridSize = gridSize;
        map = new boolean[width][height];
    }

    public void update(List<Bounds> nonPathAreas) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = false;
            }
        }
        for (var bounds : nonPathAreas) {
            markArea(bounds);
        }
    }

    public void debugDraw(World world) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = map[x][y] ? Color.RED : Color.GREEN;
                Bounds bounds = Bounds.atOrigin(origin, gridSize, gridSize).moveBy(x * gridSize, y * gridSize);
                world.drawRectangle(bounds, color.withOpacity(0.5));
            }
        }
    }

    private void markArea(Bounds bounds) {
        Vector boundsOrigin = bounds.origin();
        int xMin = (int) boundsOrigin.x() / gridSize;
        int yMin = (int) boundsOrigin.y() / gridSize;

        Vector bottomRight = bounds.bottomRight();
        int xMax = (int) bottomRight.x() / gridSize;
        int yMax = (int) bottomRight.y() / gridSize;
        for (int x = xMin; x < xMax; x++) {
            for (int y = yMin; y < yMax; y++) {
                map[x][y] = true;
            }
        }
    }
}
