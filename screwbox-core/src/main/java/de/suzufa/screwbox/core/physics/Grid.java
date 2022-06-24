package de.suzufa.screwbox.core.physics;

import static java.util.Objects.nonNull;

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

        private Node(final int x, final int y, final Node parent) {
            this.x = x;
            this.y = y;
            this.parent = parent;
        }

        private Node(final int x, final int y) {
            this(x, y, null);
        }

        public Node parent() {
            return parent;
        }

        public Node offset(final int deltaX, final int deltaY) {
            return new Node(x + deltaX, y + deltaY, this);
        }

        public List<Node> backtrackPath() {
            final List<Node> path = new ArrayList<>();
            Node point = this;
            while (nonNull(point.parent())) {
                path.add(0, point);
                point = point.parent();
            }
            return path;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final Node other = (Node) obj;
            return x == other.x && y == other.y;
        }

        @Override
        public String toString() {
            return "Node [x=" + x + ", y=" + y + "]";
        }

    }

    private final boolean[][] isBlocked;
    private final int width;
    private final int height;
    private final int gridSize;
    private final boolean diagonalMovementAllowed;
    private final Vector offset;

    public Grid(final Bounds area, final int gridSize, final boolean diagonalMovementAllowed) {
        Objects.requireNonNull(area, "Grid area must not be null");
        if (gridSize <= 0) {
            throw new IllegalArgumentException("GridSize must have value above zero");
        }
        if (area.origin().x() % gridSize != 0) {
            throw new IllegalArgumentException("Area origin x should be dividable by grid size.");
        }
        if (area.origin().y() % gridSize != 0) {
            throw new IllegalArgumentException("Area origin y should be dividable by grid size.");
        }
        this.gridSize = gridSize;
        this.offset = area.origin();
        this.width = gridValue(area.width());
        this.height = gridValue(area.height());
        isBlocked = new boolean[this.width][this.height];
        this.diagonalMovementAllowed = diagonalMovementAllowed;
    }

    public Node nodeAt(int x, int y) {
        return new Node(x, y);
    }

    public boolean isFree(int x, int y) {
        if (x < 0 || x > isBlocked.length - 1) {
            return false;
        }
        if (y < 0 || y > isBlocked[0].length - 1) {
            return false;
        }
        return !isBlocked[x][y];
    }

    public boolean isFree(final Node node) {
        return isFree(node.x, node.y);
    }

    public Vector toWorld(final Node node) {
        double x = (node.x + 0.5) * gridSize + offset.x();
        double y = (node.y + 0.5) * gridSize + offset.y();

        return Vector.of(x, y);
    }

    public Node toGrid(final Vector position) {
        final var tPos = tanslate(position);
        return new Node(gridValue(tPos.x()), gridValue(tPos.y()));
    }

    private Vector tanslate(final Vector position) {
        return position.substract(offset);
    }

    private Bounds tanslate(final Bounds area) {
        return area.moveBy(Vector.of(-offset.x(), -offset.y()));
    }

    public void blockArea(final Bounds area) {
        final var tArea = tanslate(area);
        final int minX = Math.max(gridValue(tArea.origin().x()), 0);
        final int maxX = Math.min(gridValue(tArea.bottomRight().x()), width);
        final int minY = Math.max(gridValue(tArea.origin().y()), 0);
        final int maxY = Math.min(gridValue(tArea.bottomRight().y()), height);
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                isBlocked[x][y] = true;
            }
        }
    }

    private int gridValue(final double value) {
        return (int) value / gridSize;
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

    public List<Node> allNodes() {
        final var nodes = new ArrayList<Node>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                nodes.add(new Node(x, y));
            }
        }
        return nodes;
    }

}
