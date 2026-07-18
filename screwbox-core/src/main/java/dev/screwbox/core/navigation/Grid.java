package dev.screwbox.core.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.utils.Validate;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

//TODO changelog: completely reworked grid class
public class Grid<T> implements Serializable {

    private static final int PADDING = 1;

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Returns the cell that this {@link Vector} would reside in within a grid with the specified cell size.
     *
     * @param cellSize size of cell (must be positive)
     * @since 3.12.0
     */
    public static Offset findCell(final Vector vector, final int cellSize) {
        Validate.positive(cellSize, "cell size must be positive");
        return Offset.at(Math.floorDiv((int) vector.x(), cellSize), Math.floorDiv((int) vector.y(), cellSize));
    }

    protected final int width;
    protected final int height;
    protected final int internalWidth;
    protected final int internalHeight;
    protected final int cellSize;
    protected final Bounds bounds;
    protected final T[] cellData;

    public Grid(final Bounds bounds, final int cellSize) {
        requireNonNull(bounds, "grid bounds must not be null");
        Validate.positive(cellSize, "cell size must be positive");
        Validate.isTrue(() -> bounds.width() % cellSize == 0, "bounds should fit cell size");
        Validate.isTrue(() -> bounds.height() % cellSize == 0, "bounds should fit cell size");

        this.cellSize = cellSize;
        this.bounds = bounds;
        width = toCell(bounds.width());
        height = toCell(bounds.height());
        internalWidth = width + (PADDING * 2);
        internalHeight = height + (PADDING * 2);
        cellData = (T[]) new Object[internalWidth * internalHeight];
    }

    /**
     * Returns the area of this {@link BinaryGrid} in the {@link World}.
     */
    public Bounds bounds() {
        return bounds;
    }

    protected int toCell(final double value) {
        return Math.floorDiv((int) value, cellSize);
    }

    public Vector toWorld(final Offset cell) {
        final double x = (cell.x() + 0.5) * cellSize + bounds.origin().x();
        final double y = (cell.y() + 0.5) * cellSize + bounds.origin().y();
        return Vector.$(x, y);
    }

    public Bounds cellBounds(final Offset node) {
        final Vector position = toWorld(node);
        return Bounds.atPosition(position, cellSize, cellSize);
    }

    public Offset toCell(final Vector position) {
        final var translated = position.subtract(bounds.origin());
        return BinaryGrid.findCell(translated, cellSize);
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

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int nodeCount() {
        return width * height;
    }

    public int cellSize() {
        return cellSize;
    }

    protected boolean isInGrid(final int x, final int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private int toInternalIndex(final int x, final int y) {
        return (y + PADDING) * internalWidth + (x + PADDING);
    }

    public void set(int x, int y, T value) {
        validateIsWithinGrid(x, y);
        var internalIndex = toInternalIndex(x, y);
        cellData[internalIndex] = value;
    }

    public T get(int x, int y) {
        validateIsWithinGrid(x, y);
        var internalIndex = toInternalIndex(x, y);
        return cellData[internalIndex];
    }

    private void validateIsWithinGrid(final int x, final int y) {
        if (!isInGrid(x, y)) {
            throw new IllegalArgumentException("position is not within grid: " + Offset.at(x, y));
        }
    }

    public void set(Offset cell, T value) {
        set(cell.x(), cell.y(), value);
    }

}
