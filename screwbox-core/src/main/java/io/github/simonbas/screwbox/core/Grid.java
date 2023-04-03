package io.github.simonbas.screwbox.core;

import io.github.simonbas.screwbox.core.graphics.World;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static io.github.simonbas.screwbox.core.Bounds.atPosition;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * A {@link Grid} to raster your game world. The {@link Grid} is a two
 * dimensional area with blocked and free {@link Node}s that is aligned to the
 * game world.
 */
public class Grid implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * A node in (or out of) the {@link Grid}. Can have {@link #parent()}
     * {@link Node}s when created relatively to other {@link Node}s.
     */
    public record Node(int x, int y, Node parent) {

        private Node(final int x, final int y) {
            this(x, y, null);
        }

        private Node offset(final int deltaX, final int deltaY) {
            return new Node(x + deltaX, y + deltaY, this);
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

        /**
         * Returns the distance between this {@link Node} and another {@link Node}.
         * Distance doesn't consider {@link Grid#gridSize}.
         */
        public double distance(final Node other) {
            final int deltaX = other.x - x;
            final int deltaY = other.y - y;
            return Math.sqrt((double) deltaX * deltaX + (double) deltaY * deltaY);
        }
    }

    private final BitSet isBlocked;
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
        requireNonNull(area, "grid area must not be null");

        if (gridSize <= 0) {
            throw new IllegalArgumentException("grid size must have value above zero");
        }
        if (area.origin().x() % gridSize != 0) {
            throw new IllegalArgumentException("area origin x should be dividable by grid size.");
        }
        if (area.origin().y() % gridSize != 0) {
            throw new IllegalArgumentException("area origin y should be dividable by grid size.");
        }

        this.gridSize = gridSize;
        this.offset = area.origin();
        this.width = gridValue(area.width());
        this.height = gridValue(area.height());
        this.isBlocked = new BitSet(this.width * this.height);
        this.useDiagonalSearch = useDiagonalSearch;
        this.area = area;
    }

    /**
     * Returns a new instance without any blocked {@link Node}s.
     */
    public Grid clearedInstance() {
        return new Grid(area, gridSize, useDiagonalSearch);
    }

    /**
     * Returns the area of this {@link Grid} in the {@link World}.
     */
    public Bounds area() {
        return area;
    }

    /**
     * Returns the {@link Node} at the given Position. May return {@link Node}s out
     * of grid. This can be checked via {@link #isInGrid(Node)}.
     */
    public Node nodeAt(final int x, final int y) {
        return new Node(x, y);
    }

    /**
     * Retruns true if the given position is not blocked and inside the {@link Grid}.
     */
    public boolean isFree(final int x, final int y) {
        return isInGrid(x, y) && !isBlocked.get(x * width + y);
    }

    private boolean isInGrid(final int x, final int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public boolean isFree(final Node node) {
        return isFree(node.x, node.y);
    }

    public Vector worldPosition(final Node node) {
        final double x = (node.x + 0.5) * gridSize + offset.x();
        final double y = (node.y + 0.5) * gridSize + offset.y();
        return Vector.$(x, y);
    }

    /**
     * Returns the corresponding area of the {@link Node} in the {@link Grid} in the
     * game world. Returns valid areas, even if the {@link Node} is out of the
     * {@link Grid}.
     */
    public Bounds worldArea(final Node node) {
        final Vector position = worldPosition(node);
        return atPosition(position, gridSize, gridSize);
    }

    public Node toGrid(final Vector position) {
        final var translated = position.substract(offset);
        return new Node(gridValue(translated.x()), gridValue(translated.y()));
    }

    private Bounds tanslate(final Bounds area) {
        return area.moveBy(-offset.x(), -offset.y());
    }

    public void freeArea(final Bounds area) {
        markArea(area, false);
    }

    public void freeAt(final Vector position) {
        statusChangeAt(position, false);
    }

    public void blockAt(final Vector position) {
        statusChangeAt(position, true);
    }

    public void block(final Node node) {
        block(node.x, node.y);
    }

    public void block(final int x, final int y) {
        statusChange(x, y, true);
    }

    private void statusChange(final int x, final int y, final boolean status) {
        if (isInGrid(x, y)) {
            isBlocked.set(x * width + y, status);
        }
    }

    private void statusChangeAt(final Vector position, boolean status) {
        final Node node = toGrid(position);
        statusChange(node.x, node.y, status);
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
                isBlocked.set(x * width + y, status);
            }
        }
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public List<Node> blockedNeighbors(final Node node) {
        final List<Node> neighbors = new ArrayList<>();
        for (final var neighbor : neighbors(node)) {
            if (isBlocked(neighbor)) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    private boolean isInGrid(final Node node) {
        return node.x > 0 && node.x < width && node.y > 0 && node.y < height;
    }

    public List<Node> neighbors(final Node node) {
        final List<Node> neighbors = new ArrayList<>();

        var nodes = useDiagonalSearch
                ? List.of(
                node.offset(0, 1),
                node.offset(0, -1),
                node.offset(-1, 0),
                node.offset(1, 0),
                node.offset(-1, 1),
                node.offset(1, 1),
                node.offset(-1, -1),
                node.offset(1, -1))
                : List.of(
                node.offset(0, 1),
                node.offset(0, -1),
                node.offset(-1, 0),
                node.offset(1, 0));

        for (var n : nodes) {
            if (isInGrid(n)) {
                neighbors.add(n);
            }
        }
        return neighbors;
    }

    public List<Node> reachableNeighbors(final Node node) {
        final List<Node> neighbors = new ArrayList<>();
        final Consumer<Node> addIfFree = nde -> {
            if (isFree(nde)) {
                neighbors.add(nde);
            }
        };

        final Node down = node.offset(0, 1);
        final Node up = node.offset(0, -1);
        final Node left = node.offset(-1, 0);
        final Node right = node.offset(1, 0);
        addIfFree.accept(down);
        addIfFree.accept(up);
        addIfFree.accept(left);
        addIfFree.accept(right);

        if (!useDiagonalSearch) {
            return neighbors;
        }
        final Node downLeft = node.offset(-1, 1);
        final Node downRight = node.offset(1, 1);

        if (isFree(down)) {
            if (isFree(right)) {
                addIfFree.accept(downRight);
            }
            if (isFree(left)) {
                addIfFree.accept(downLeft);
            }
        }

        final Node upLeft = node.offset(-1, -1);
        final Node upRight = node.offset(1, -1);
        if (isFree(up)) {
            if (isFree(upLeft) && isFree(left)) {
                addIfFree.accept(upLeft);
            }
            if (isFree(upRight) && isFree(right)) {
                addIfFree.accept(upRight);
            }
        }
        return neighbors;
    }

    /**
     * Returns all {@link Node}s in the {@link Grid}.
     */
    public List<Node> nodes() {
        final var nodes = new ArrayList<Node>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                nodes.add(new Node(x, y));
            }
        }
        return nodes;
    }

    /**
     * Returns the count of {@link Node}s in the {@link Grid}.
     */
    public int nodeCount() {
        return width * height;
    }

    public int blockedCount() {
        return nodeCount() - freeCount();
    }

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

    public List<Node> backtrack(final Node node) {
        return backtrack(node, new ArrayList<>());
    }

    public Vector snap(final Vector position) {
        final Node node = toGrid(position);
        return worldPosition(node);
    }

    public boolean isBlocked(final int x, final int y) {
        return isInGrid(x, y) && isBlocked.get(x * width + y);
    }

    public boolean isBlocked(final Node node) {
        return isBlocked(node.x, node.y);
    }

    private List<Node> backtrack(final Node node, final List<Node> path) {
        if (nonNull(node.parent)) {
            path.add(0, node);
            backtrack(node.parent, path);
        }
        return path;
    }

    private int gridValue(final double value) {
        return Math.floorDiv((int) value, gridSize);
    }
}