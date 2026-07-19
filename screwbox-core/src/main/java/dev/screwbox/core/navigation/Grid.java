package dev.screwbox.core.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.utils.Validate;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * A grid aligned to the game world. Stores any kind of data within cells.
 */
public class Grid<T extends Serializable> implements Serializable {

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


    /**
     * Creates an instance of a boolean grid.
     */
    public static Grid<Boolean> booleanGrid(final Bounds bounds, final int cellSize) {
        return new Grid<>(bounds, cellSize, Boolean.class);
    }

    /**
     * Creates a new instance at the specified bounds with the specified cell size.
     * Cells must fit exactly within the bounds.
     */
    public Grid(final Bounds bounds, final int cellSize, Class<T> type) {
        requireNonNull(bounds, "grid bounds must not be null");
        Validate.positive(cellSize, "cell size must be positive");
        Validate.isTrue(() -> bounds.width() % cellSize == 0, "bounds should fit cell size");
        Validate.isTrue(() -> bounds.height() % cellSize == 0, "bounds should fit cell size");

        this.cellSize = cellSize;
        this.bounds = bounds;
        width = toCell(bounds.width());
        height = toCell(bounds.height());
        this.cellData = (T[]) Array.newInstance(type, width * height);
    }

    /**
     * Fills all cells with the specified data.
     *
     * @since 3.33.0
     */
    public void fill(T value) {
        Arrays.fill(cellData, value);
    }

    //TODO document, test, changelog
    public void fill(Supplier<T> value) {
        for (int i = 0; i < cellData.length; i++) {
            cellData[i] = value.get();
        }
    }

    /**
     * Returns the {@link Bounds} of this {@link Grid} in the {@link Environment}.
     */
    public Bounds bounds() {
        return bounds;
    }

    /**
     * Returns the cell position of the specified cell within the world.  Will return positions that reside outside the grid.
     */
    public Vector cellPosition(final Offset cell) {
        final double x = (cell.x() + 0.5) * cellSize + bounds.origin().x();
        final double y = (cell.y() + 0.5) * cellSize + bounds.origin().y();
        return Vector.$(x, y);
    }

    /**
     * Returns the cell {@link Bounds} of the specified cell within the world. Will return {@link Bounds} that reside outside the grid.
     */
    public Bounds cellBounds(final Offset cell) {
        final Vector position = cellPosition(cell);
        return Bounds.atPosition(position, cellSize, cellSize);
    }

    /**
     * Returns the cell at the specified positon. Will return cells that reside outside the grid.
     */
    public Offset toCell(final Vector position) {
        final var translated = position.subtract(bounds.origin());
        return Grid.findCell(translated, cellSize);
    }

    /**
     * Returns a list of all cells within the grid.
     */
    public List<Offset> cells() {
        final var nodes = new ArrayList<Offset>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                nodes.add(Offset.at(x, y));
            }
        }
        return nodes;
    }

    /**
     * Returns the horizontal number of cells within the grid.
     */
    public int width() {
        return width;
    }

    /**
     * Returns the vertical number of cells within the grid.
     */
    public int height() {
        return height;
    }

    /**
     * Returns the number of cells within the grid.
     */
    public int cellCount() {
        return width * height;
    }

    /**
     * Returns the size of a single cell within the grid.
     */
    public int cellSize() {
        return cellSize;
    }


    /**
     * Returns {@code true} if the specified cell exits within the grid.
     */
    public boolean contains(final Offset cell) {
        return contains(cell.x(), cell.y());
    }

    /**
     * Returns {@code true} if the specified cell exits within the grid.
     */
    public boolean contains(final int x, final int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Clears the contents of the cell at the specified position.
     */
    public void clear(final Vector position) {
        clear(toCell(position));
    }

    /**
     * Clears the contents of the specified cell.
     */
    public void clear(final Offset cell) {
        clear(cell.x(), cell.y());
    }

    /**
     * Clears the contents of the specified cell.
     */
    public void clear(final int x, final int y) {
        set(x, y, null);
    }

    /**
     * Sets the contents at the specified position.
     */
    public void set(final Vector position, final T value) {
        set(toCell(position), value);
    }

    /**
     * Sets the contents of the specified cell.
     */
    public void set(Offset cell, T value) {
        set(cell.x(), cell.y(), value);
    }

    /**
     * Sets the contents of the specified cell.
     */
    public void set(final int x, final int y, final T value) {
        validateCell(x, y);
        final var internalIndex = toDataIndex(x, y);
        cellData[internalIndex] = value;
    }

    /**
     * Returns the contents of the specified cell.
     */
    public T get(final Offset cell) {
        return get(cell.x(), cell.y());
    }

    /**
     * Returns the contents of the specified cell.
     */
    public T get(final int x, final int y) {
        validateCell(x, y);
        final var internalIndex = toDataIndex(x, y);
        return cellData[internalIndex];
    }

    /**
     * Sets the contents of all cells within the specified area.
     */
    public void fill(final Bounds area, final T value) {
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

    /**
     * Returns {@code true} if the specified cell exists and has a value.
     */
    public boolean hasValue(final Offset cell) {
        return hasValue(cell.x(), cell.y());
    }

    /**
     * Returns {@code true} if the specified cell exists and has a value.
     */
    public boolean hasValue(int x, int y) {
        return contains(x, y) && get(x, y) != null;
    }

    private int toCell(final double value) {
        return Math.floorDiv((int) value, cellSize);
    }

    private void validateCell(final int x, final int y) {
        if (!contains(x, y)) {
            throw new IllegalArgumentException("position is not within grid: " + Offset.at(x, y));
        }
    }

    private int toDataIndex(final int x, final int y) {
        return y * width + x;
    }

}
