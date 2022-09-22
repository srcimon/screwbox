package de.suzufa.screwbox.core.physics;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;

public class Grid {

    public class Node {

        final int x;
        final int y;
        final Node parent;

        private Node(final int x, final int y) {
            this(x, y, null);
        }

        private Node(final int x, final int y, final Node parent) {
            this.x = x;
            this.y = y;
            this.parent = parent;
        }

        private Node offset(final int deltaX, final int deltaY) {
            return new Node(x + deltaX, y + deltaY, this);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        public int x() {
            return x;
        }

        public int y() {
            return y;
        }

        public Node getParent() {
            return parent;
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
    private final boolean useDiagonalMovement;
    private final Vector offset;

    public Grid(final Bounds area, final int gridSize) {
        this(area, gridSize, true);
    }

    public Grid(final Bounds area, final int gridSize, final boolean useDiagonalMovement) {
        requireNonNull(area, "Grid area must not be null");

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
        this.useDiagonalMovement = useDiagonalMovement;
    }

    public Node nodeAt(final int x, final int y) {
        return new Node(x, y);
    }

    public boolean isFree(final int x, final int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }
        return !isBlocked[x][y];
    }

    public boolean isFree(final Node node) {
        return isFree(node.x, node.y);
    }

    public Vector toWorld(final Node node) {
        final double x = (node.x + 0.5) * gridSize + offset.x();
        final double y = (node.y + 0.5) * gridSize + offset.y();

        return Vector.of(x, y);
    }

    // TODO: test
    public Bounds toWorldBounds(final Node node) {
        Vector position = toWorld(node);
        return Bounds.atPosition(position, gridSize, gridSize);
    }

    public Node toGrid(final Vector position) {
        final var translated = tanslate(position);
        return new Node(gridValue(translated.x()), gridValue(translated.y()));
    }

    private Vector tanslate(final Vector position) {
        return position.substract(offset);
    }

    private Bounds tanslate(final Bounds area) {
        return area.moveBy(-offset.x(), -offset.y());
    }

    public void blockArea(final Bounds area) {
        final var tArea = tanslate(area).inflated(-0.1);
        final int minX = Math.max(gridValue(tArea.origin().x()), 0);
        final int maxX = Math.min(gridValue(tArea.bottomRight().x()), width - 1);
        final int minY = Math.max(gridValue(tArea.origin().y()), 0);
        final int maxY = Math.min(gridValue(tArea.bottomRight().y()), height - 1);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                block(x, y);
            }
        }
    }

    // TODO: Test
    public void block(Node node) {
        block(node.x, node.y);
    }

    private void block(int x, int y) {
        isBlocked[x][y] = true;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public List<Node> neighbors(final Node node) {
        final List<Node> neighbors = new ArrayList<>();
        final Node down = node.offset(0, 1);
        final Node up = node.offset(0, -1);
        final Node left = node.offset(-1, 0);
        final Node right = node.offset(1, 0);
        addIfFree(neighbors, down);
        addIfFree(neighbors, up);
        addIfFree(neighbors, left);
        addIfFree(neighbors, right);

        if (!useDiagonalMovement) {
            return neighbors;
        }
        final Node downLeft = node.offset(-1, 1);
        final Node downRight = node.offset(1, 1);
        if (isFree(down)) {
            if (isFree(right)) {
                addIfFree(neighbors, downRight);
            }
            if (isFree(left)) {
                addIfFree(neighbors, downLeft);
            }
        }

        final Node upLeft = node.offset(-1, -1);
        final Node upRight = node.offset(1, -1);
        if (isFree(up)) {
            if (isFree(upLeft) && isFree(left)) {
                addIfFree(neighbors, upLeft);
            }
            if (isFree(upRight) && isFree(right)) {
                addIfFree(neighbors, upRight);
            }
        }
        return neighbors;
    }

    private void addIfFree(final List<Node> neighbors, final Node node) {
        if (isFree(node)) {
            neighbors.add(node);
        }
    }

    public List<Node> nodes() {
        final var nodes = new ArrayList<Node>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                nodes.add(new Node(x, y));
            }
        }
        return nodes;
    }

    private int gridValue(final double value) {
        return Math.floorDiv((int) value, gridSize);
    }

    public List<Node> backtrack(Node node) {
        final List<Node> path = new ArrayList<>();
        while (nonNull(node.parent)) {
            path.add(0, node);
            node = node.parent;
        }
        return path;
    }

}