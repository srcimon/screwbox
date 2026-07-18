package dev.screwbox.core.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.utils.Validate;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

//TODO add javadoc
public class Grid<T> implements Serializable {

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
    protected final int cellSize;
    protected final Bounds bounds;
    protected final T[] cellData;

    @SuppressWarnings("unchecked")
    public Grid(final Bounds bounds, final int cellSize) {
        requireNonNull(bounds, "grid bounds must not be null");
        Validate.positive(cellSize, "cell size must be positive");
        Validate.isTrue(() -> bounds.width() % cellSize == 0, "bounds should fit cell size");
        Validate.isTrue(() -> bounds.height() % cellSize == 0, "bounds should fit cell size");

        this.cellSize = cellSize;
        this.bounds = bounds;
        width = toCell(bounds.width());
        height = toCell(bounds.height());
        cellData = (T[]) new Object[width * height];
    }

    public void initialize(T value) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                set(x, y, value);
            }
        }
    }

    /**
     * Returns the {@link Bounds} of this {@link Grid} in the {@link Environment}.
     */
    public Bounds bounds() {
        return bounds;
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
        return Grid.findCell(translated, cellSize);
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

    public boolean contains(final Offset cell) {
        return contains(cell.x(), cell.y());
    }

    public boolean contains(final int x, final int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private int toInternalIndex(final int x, final int y) {
        return y * width + x;
    }

    public void clear(Vector position) {
        clear(toCell(position));
    }

    public void clear(final Offset cell) {
        clear(cell.x(), cell.y());
    }

    public void clear(final int x, final int y) {
        set(x, y, null);
    }

    public void set(Vector position, T value) {
        set(toCell(position), value);
    }

    public void set(int x, int y, T value) {
        validateIsWithinGrid(x, y);
        var internalIndex = toInternalIndex(x, y);
        cellData[internalIndex] = value;
    }

    public T get(Offset cell) {
        return get(cell.x(), cell.y());
    }

    public T get(int x, int y) {
        validateIsWithinGrid(x, y);
        var internalIndex = toInternalIndex(x, y);
        return cellData[internalIndex];
    }

    public void set(Offset cell, T value) {
        set(cell.x(), cell.y(), value);
    }

    public void set(final Bounds area, final T value) {
        final var areaTranslated = area.moveBy(-this.bounds.origin().x(), -this.bounds.origin().y()).expand(-0.1);
        final int minX = Math.max(toCell(areaTranslated.origin().x()), 0);
        final int maxX = Math.min(toCell(areaTranslated.bottomRight().x()), width - 1);
        final int minY = Math.max(toCell(areaTranslated.origin().y()), 0);
        final int maxY = Math.min(toCell(areaTranslated.bottomRight().y()), height - 1);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                set(x, y, value);
            }
        }
    }

    public boolean hasValue(final Offset cell) {
        final int x = cell.x();
        final int y = cell.y();
        return hasValue(x, y);
    }

    public boolean hasValue(int x, int y) {
        return contains(x, y) && get(x, y) != null;
    }

    private int toCell(final double value) {
        return Math.floorDiv((int) value, cellSize);
    }

    private void validateIsWithinGrid(final int x, final int y) {
        if (!contains(x, y)) {
            throw new IllegalArgumentException("position is not within grid: " + Offset.at(x, y));
        }
    }
}
