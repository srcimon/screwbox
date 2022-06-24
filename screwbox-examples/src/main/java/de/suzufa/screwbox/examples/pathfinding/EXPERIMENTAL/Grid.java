package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;

public class Grid {

    private final boolean[][] isBlocked;
    private final int width;
    private final int height;
    private final int gridSize;

    public Grid(final Bounds bounds, final int gridSize) {
        this.gridSize = gridSize;
        width = gridValue(bounds.width());
        height = gridValue(bounds.height());
        isBlocked = new boolean[width][height];
    }

    public boolean isFree(final GridNode node) {
        if (node.y() < 0 || node.y() > isBlocked[0].length - 1) {
            return false;
        }
        if (node.x() < 0 || node.x() > isBlocked.length - 1) {
            return false;
        }
        return !isBlocked[node.x()][node.y()];
    }

    public Vector toWorld(final GridNode node) {
        return Vector.of((node.x() + 0.5) * gridSize, (node.y() + 0.5) * gridSize);
    }

    public GridNode toGrid(final Vector position) {
        return new GridNode(gridValue(position.x()), gridValue(position.y()), null);
    }

    public void blockArea(final Bounds area) {
        final int minX = gridValue(area.origin().x());
        final int maxX = gridValue(area.bottomRight().x());
        final int minY = gridValue(area.origin().y());
        final int maxY = gridValue(area.bottomRight().y());
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
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
    public List<GridNode> findNeighbors(final GridNode node) {
        final List<GridNode> neighbors = new ArrayList<>();
        final GridNode downLeft = node.offset(-1, 1);
        final GridNode downRight = node.offset(1, 1);
        final GridNode upLeft = node.offset(-1, -1);
        final GridNode upRight = node.offset(1, -1);
        final GridNode down = node.offset(0, 1);
        final GridNode up = node.offset(0, -1);
        final GridNode left = node.offset(-1, 0);
        final GridNode right = node.offset(1, 0);
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

    private int gridValue(final double value) {
        return (int) value / gridSize;
    }

}
