package de.suzufa.screwbox.core;

import static de.suzufa.screwbox.core.Bounds.atPosition;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Grid implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private final boolean useDiagonalSearch;
    private final Vector offset;
    private final Bounds area;

    public Grid(final Bounds area, final int gridSize) {
        this(area, gridSize, true);
    }

    public Grid(final Bounds area, final int gridSize, final boolean useDiagonalSearch) {
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
        this.useDiagonalSearch = useDiagonalSearch;
        this.area = area;
    }

    public Grid cleared() {
        return new Grid(area, gridSize, useDiagonalSearch);
    }

    public Bounds area() {
        return area;
    }

    public Node nodeAt(final int x, final int y) {
        return new Node(x, y);
    }

    public boolean isFree(final int x, final int y) {
        return isInGrid(x, y) && !isBlocked[x][y];
    }

    private boolean isInGrid(final int x, final int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
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
        final Vector position = toWorld(node);
        return atPosition(position, gridSize, gridSize);
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

    public void freeAt(final Vector position) {
        final Node node = toGrid(position);
        if (isInGrid(node)) {
            isBlocked[node.x][node.y] = false;
        }
    }

    public void freeArea(final Bounds area) {
        markArea(area, false);
    }

    public void blockAt(final Vector position) {
        final Node node = toGrid(position);
        if (isInGrid(node)) {
            isBlocked[node.x][node.y] = true;
        }
    }

    public void blockArea(final Bounds area) {
        markArea(area, true);
    }

    private void markArea(final Bounds area, final boolean status) {
        final var areaTranslated = tanslate(area).inflated(-0.1);
        final int minX = Math.max(gridValue(areaTranslated.origin().x()), 0);
        final int maxX = Math.min(gridValue(areaTranslated.bottomRight().x()), width - 1);
        final int minY = Math.max(gridValue(areaTranslated.origin().y()), 0);
        final int maxY = Math.min(gridValue(areaTranslated.bottomRight().y()), height - 1);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                isBlocked[x][y] = status;
            }
        }
    }

    public void block(final Node node) {
        block(node.x, node.y);
    }

    public void block(final int x, final int y) {
        isBlocked[x][y] = true;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    // TODO: Combine three methods
    // TODO: test and javadoc
    public List<Node> blockedNeighbors(final Node node) {
        final List<Node> neighbors = new ArrayList<>();
        final Node down = node.offset(0, 1);
        final Node up = node.offset(0, -1);
        final Node left = node.offset(-1, 0);
        final Node right = node.offset(1, 0);

        addIfInGridAndBlocked(neighbors, down);
        addIfInGridAndBlocked(neighbors, up);
        addIfInGridAndBlocked(neighbors, left);
        addIfInGridAndBlocked(neighbors, right);

        if (!useDiagonalSearch) {
            return neighbors;
        }
        final Node downLeft = node.offset(-1, 1);
        final Node downRight = node.offset(1, 1);
        final Node upLeft = node.offset(-1, -1);
        final Node upRight = node.offset(1, -1);

        addIfInGridAndBlocked(neighbors, downLeft);
        addIfInGridAndBlocked(neighbors, downRight);
        addIfInGridAndBlocked(neighbors, upLeft);
        addIfInGridAndBlocked(neighbors, upRight);
        return neighbors;
    }

    private boolean isInGrid(final Node node) {
        return node.x > 0 && node.x < width && node.y > 0 && node.y < height;
    }

    public List<Node> neighbors(final Node node) {
        final List<Node> neighbors = new ArrayList<>();
        final Node down = node.offset(0, 1);
        final Node up = node.offset(0, -1);
        final Node left = node.offset(-1, 0);
        final Node right = node.offset(1, 0);

        addIfInGrid(neighbors, down);
        addIfInGrid(neighbors, up);
        addIfInGrid(neighbors, left);
        addIfInGrid(neighbors, right);

        if (!useDiagonalSearch) {
            return neighbors;
        }
        final Node downLeft = node.offset(-1, 1);
        final Node downRight = node.offset(1, 1);
        final Node upLeft = node.offset(-1, -1);
        final Node upRight = node.offset(1, -1);

        addIfInGrid(neighbors, downLeft);
        addIfInGrid(neighbors, downRight);
        addIfInGrid(neighbors, upLeft);
        addIfInGrid(neighbors, upRight);
        return neighbors;
    }

    public List<Node> reachableNeighbors(final Node node) {
        final List<Node> neighbors = new ArrayList<>();
        final Node down = node.offset(0, 1);
        final Node up = node.offset(0, -1);
        final Node left = node.offset(-1, 0);
        final Node right = node.offset(1, 0);
        addIfFree(neighbors, down);
        addIfFree(neighbors, up);
        addIfFree(neighbors, left);
        addIfFree(neighbors, right);

        if (!useDiagonalSearch) {
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

    private void addIfInGridAndBlocked(final List<Node> neighbors, final Node node) {
        if (isInGrid(node) && !isFree(node)) {
            neighbors.add(node);
        }
    }

    private void addIfInGrid(final List<Node> neighbors, final Node node) {
        if (isInGrid(node)) {
            neighbors.add(node);
        }
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

    // TODO: test and javadoc
    public int blockedCount() {
        return width * height - freeCount();
    }

    // TODO: test and javadoc
    public int freeCount() {
        int freeCount = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y <= height; y++) {
                if (isFree(x, y)) {
                    freeCount++;
                }
            }
        }
        return freeCount;
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

    public Vector snap(final Vector position) {
        final Node node = toGrid(position);
        return toWorld(node);
    }

    public boolean isBlocked(final int x, final int y) {
        return !isFree(x, y);
    }

    public boolean isBlocked(final Node node) {
        return isBlocked(node.x, node.y);
    }

}