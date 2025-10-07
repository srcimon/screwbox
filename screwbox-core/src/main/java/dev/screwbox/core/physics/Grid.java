package dev.screwbox.core.physics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.utils.Validate;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class Grid implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
        Validate.positive(gridSize, "grid size must be positive");
        Validate.isTrue(() -> area.origin().x() % gridSize == 0, "area origin x should be dividable by grid size.");
        Validate.isTrue(() -> area.origin().y() % gridSize == 0, "area origin y should be dividable by grid size.");

        this.gridSize = gridSize;
        this.offset = area.origin();
        this.width = gridValue(area.width());
        this.height = gridValue(area.height());
        this.isBlocked = new BitSet(this.width * this.height);
        this.useDiagonalSearch = useDiagonalSearch;
        this.area = area;
    }

    /**
     * Returns the area of this {@link Grid} in the {@link World}.
     */
    public Bounds area() {
        return area;
    }

    public Offset nodeAt(final int x, final int y) {
        return Offset.at(x, y);
    }

    /**
     * Returns {@code true} if the specified position is not blocked and inside the {@link Grid}.
     */
    public boolean isFree(final int x, final int y) {
        return isInGrid(x, y) && !isBlocked.get(getBitIndex(x, y));
    }

    private boolean isInGrid(final int x, final int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public boolean isFree(final Offset node) {
        return isFree(node.x(), node.y());
    }

    public Vector worldPosition(final Offset node) {
        final double x = (node.x() + 0.5) * gridSize + offset.x();
        final double y = (node.y() + 0.5) * gridSize + offset.y();
        return Vector.$(x, y);
    }

    public Bounds worldArea(final Offset node) {
        final Vector position = worldPosition(node);
        return Bounds.atPosition(position, gridSize, gridSize);
    }

    public Offset toGrid(final Vector position) {
        final var translated = position.substract(offset);
        return Offset.at(gridValue(translated.x()), gridValue(translated.y()));
    }

    private Bounds translate(final Bounds area) {
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

    public void block(final Offset node) {
        block(node.x(), node.y());
    }

    public void block(final int x, final int y) {
        statusChange(x, y, true);
    }

    private void statusChange(final int x, final int y, final boolean status) {
        if (isInGrid(x, y)) {
            isBlocked.set(getBitIndex(x, y), status);
        }
    }

    private void statusChangeAt(final Vector position, boolean status) {
        final Offset node = toGrid(position);
        statusChange(node.x(), node.y(), status);
    }

    public void blockArea(final Bounds area) {
        markArea(area, true);
    }

    private void markArea(final Bounds area, final boolean status) {
        final var areaTranslated = translate(area).expand(-0.1);
        final int minX = Math.max(gridValue(areaTranslated.origin().x()), 0);
        final int maxX = Math.min(gridValue(areaTranslated.bottomRight().x()), width - 1);
        final int minY = Math.max(gridValue(areaTranslated.origin().y()), 0);
        final int maxY = Math.min(gridValue(areaTranslated.bottomRight().y()), height - 1);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                isBlocked.set(getBitIndex(x, y), status);
            }
        }
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public List<Offset> blockedNeighbors(final Offset node) {
        final List<Offset> neighbors = new ArrayList<>();
        for (final var neighbor : neighbors(node)) {
            if (isBlocked(neighbor)) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    private boolean isInGrid(final Offset node) {
        return node.x() > 0 && node.x() < width && node.y() > 0 && node.y() < height;
    }

    public List<Offset> neighbors(final Offset node) {
        final List<Offset> neighbors = new ArrayList<>();

        var nodes = useDiagonalSearch
                ? List.of(
                node.add(0, 1),
                node.add(0, -1),
                node.add(-1, 0),
                node.add(1, 0),
                node.add(-1, 1),
                node.add(1, 1),
                node.add(-1, -1),
                node.add(1, -1))
                : List.of(
                node.add(0, 1),
                node.add(0, -1),
                node.add(-1, 0),
                node.add(1, 0));

        for (var n : nodes) {
            if (isInGrid(n)) {
                neighbors.add(n);
            }
        }
        return neighbors;
    }

    public List<Offset> reachableNeighbors(final Offset node) {
        final List<Offset> neighbors = new ArrayList<>();
        final Consumer<Offset> addIfFree = nde -> {
            if (isFree(nde)) {
                neighbors.add(nde);
            }
        };

        final Offset down = node.addY(1);
        final Offset up = node.addY(-1);
        final Offset left = node.addX(-1);
        final Offset right = node.addX(1);
        addIfFree.accept(down);
        addIfFree.accept(up);
        addIfFree.accept(left);
        addIfFree.accept(right);

        if (!useDiagonalSearch) {
            return neighbors;
        }
        final Offset downLeft = node.add(-1, 1);
        final Offset downRight = node.add(1, 1);

        if (isFree(down)) {
            if (isFree(right)) {
                addIfFree.accept(downRight);
            }
            if (isFree(left)) {
                addIfFree.accept(downLeft);
            }
        }

        final Offset upLeft = node.add(-1, -1);
        final Offset upRight = node.add(1, -1);
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

    public List<Offset> nodes() {
        final var nodes = new ArrayList<Offset>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                nodes.add(Offset.at(x, y));
            }
        }
        return nodes;
    }

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

    public Vector snap(final Vector position) {
        final Offset node = toGrid(position);
        return worldPosition(node);
    }

    public boolean isBlocked(final int x, final int y) {
        return isInGrid(x, y) && isBlocked.get(getBitIndex(x, y));
    }

    private int getBitIndex(int x, int y) {
        return x * height + y;
    }

    public boolean isBlocked(final Offset node) {
        return isBlocked(node.x(), node.y());
    }

    private int gridValue(final double value) {
        return Math.floorDiv((int) value, gridSize);
    }

    public int gridSize() {
        return gridSize;
    }
}