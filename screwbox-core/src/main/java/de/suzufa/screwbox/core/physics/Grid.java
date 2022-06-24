package de.suzufa.screwbox.core.physics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;

public class Grid {

    public class Node {
        private final int x;
        private final int y;
        private final Node parent;

        public Node(final int x, final int y, final Node parent) {
            this.x = x;
            this.y = y;
            this.parent = parent;
        }

        public Node(int x, int y) {
            this(x, y, null);
        }

        public int x() {
            return x;
        }

        public int y() {
            return y;
        }

        public Node parent() {
            return parent;
        }

        public Node offset(final int deltaX, final int deltaY) {
            return new Node(x + deltaX, y + deltaY, this);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Node other = (Node) obj;
            return x == other.x && y == other.y;
        }
    }

    private final boolean[][] isBlocked;
    private final int width;
    private final int height;
    private final int gridSize;
    private boolean diagonalMovementAllowed;

    public Grid(double width, double height, int gridSize, boolean diagonalMovementAllowed) {
        if (width <= 0) {
            throw new IllegalArgumentException("Width must have value above zero");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Height must have value above zero");
        }
        if (gridSize <= 0) {
            throw new IllegalArgumentException("GridSize must have value above zero");
        }
        this.gridSize = gridSize;
        this.width = gridValue(width);
        this.height = gridValue(height);
        isBlocked = new boolean[this.width][this.height];
        this.diagonalMovementAllowed = diagonalMovementAllowed;
    }

    public boolean isFree(final Node node) {
        if (node.y < 0 || node.y() > isBlocked[0].length - 1) {
            return false;
        }
        if (node.x < 0 || node.x() > isBlocked.length - 1) {
            return false;
        }
        return !isBlocked[node.x][node.y];
    }

    public Vector toWorld(final Node node) {
        return Vector.of((node.x() + 0.5) * gridSize, (node.y + 0.5) * gridSize);
    }

    public Node toGrid(final Vector position) {
        return new Node(gridValue(position.x()), gridValue(position.y()), null);
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

    public List<Node> findNeighbors(final Node node) {
        final List<Node> neighbors = new ArrayList<>();
        final Node downLeft = node.offset(-1, 1);
        final Node downRight = node.offset(1, 1);
        final Node upLeft = node.offset(-1, -1);
        final Node upRight = node.offset(1, -1);
        final Node down = node.offset(0, 1);
        final Node up = node.offset(0, -1);
        final Node left = node.offset(-1, 0);
        final Node right = node.offset(1, 0);
        if (isFree(down))
            neighbors.add(down);
        if (isFree(up))
            neighbors.add(up);
        if (isFree(left))
            neighbors.add(left);
        if (isFree(right))
            neighbors.add(right);
        if (diagonalMovementAllowed) {
            if (isFree(downRight) && isFree(down) && isFree(right))
                neighbors.add(downRight);
            if (isFree(downLeft) && isFree(down) && isFree(left))
                neighbors.add(downLeft);
            if (isFree(upLeft) && isFree(up) && isFree(left))
                neighbors.add(upLeft);
            if (isFree(upRight) && isFree(up) && isFree(right))
                neighbors.add(upRight);
        }
        return neighbors;
    }

    private int gridValue(final double value) {
        return (int) value / gridSize;
    }

    public List<Node> allNodes() {
        var nodes = new ArrayList<Node>();
        for (int x = 0; x < isBlocked.length; x++) {
            for (int y = 0; y < isBlocked[0].length; y++) {
                nodes.add(new Node(x, y));
            }
        }
        return nodes;
    }

}
