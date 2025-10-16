package dev.screwbox.core.navigation;

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
    private final int cellSize;
    private final Vector offset;
    private final Bounds bounds;

    public Grid(final Bounds bounds, final int cellSize) {
        requireNonNull(bounds, "grid bounds must not be null");
        Validate.positive(cellSize, "cell size must be positive");
        Validate.isTrue(() -> bounds.origin().x() % cellSize == 0, "bounds origin x should be dividable by cell size.");
        Validate.isTrue(() -> bounds.origin().y() % cellSize == 0, "bounds origin y should be dividable by cell size.");

        this.cellSize = cellSize;
        this.offset = bounds.origin();
        this.width = gridValue(bounds.width());
        this.height = gridValue(bounds.height());
        this.isBlocked = new BitSet(this.width * this.height);
        this.bounds = bounds;
    }

    /**
     * Returns the area of this {@link Grid} in the {@link World}.
     */
    public Bounds bounds() {
        return bounds;
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
        final double x = (node.x() + 0.5) * cellSize + offset.x();
        final double y = (node.y() + 0.5) * cellSize + offset.y();
        return Vector.$(x, y);
    }

    public Bounds worldArea(final Offset node) {
        final Vector position = worldPosition(node);
        return Bounds.atPosition(position, cellSize, cellSize);
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

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public List<Offset> blockedSurroundingNodes(final Offset node) {
        final List<Offset> neighbors = new ArrayList<>();
        for (final var neighbor : surroundingNodes(node)) {
            if (isBlocked(neighbor)) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    public List<Offset> adjacentNodes(final Offset node) {
        final List<Offset> neighbors = new ArrayList<>();

        var nodes = List.of(
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

    public List<Offset> surroundingNodes(final Offset node) {
        final List<Offset> neighbors = new ArrayList<>();

        var nodes = List.of(
                node.add(0, 1),
                node.add(0, -1),
                node.add(-1, 0),
                node.add(1, 0),
                node.add(-1, 1),
                node.add(1, 1),
                node.add(-1, -1),
                node.add(1, -1));

        for (var n : nodes) {
            if (isInGrid(n)) {
                neighbors.add(n);
            }
        }
        return neighbors;
    }

    public List<Offset> freeAdjacentNodes(final Offset node) {
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

        return neighbors;
    }

    public List<Offset> freeSurroundingNodes(final Offset node) {
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

    public Vector snap(final Vector position) {
        final Offset node = toGrid(position);
        return worldPosition(node);
    }

    public boolean isBlocked(final int x, final int y) {
        return isInGrid(x, y) && isBlocked.get(getBitIndex(x, y));
    }

    public int cellSize() {
        return cellSize;
    }

    public boolean isBlocked(final Offset node) {
        return isBlocked(node.x(), node.y());
    }

    private int gridValue(final double value) {
        return Math.floorDiv((int) value, cellSize);
    }

    private int getBitIndex(int x, int y) {
        return x * height + y;
    }

    private boolean isInGrid(final Offset node) {
        return node.x() > 0 && node.x() < width && node.y() > 0 && node.y() < height;
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
}