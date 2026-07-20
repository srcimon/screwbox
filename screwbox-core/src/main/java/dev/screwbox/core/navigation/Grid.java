package dev.screwbox.core.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
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

    private final Size size;
    private final T[] cellData;
    private Bounds bounds;
    private double cellWidth;
    private double cellHeight;

    /**
     * Creates a new instance with the specified cell size.
     */
    public static <T extends Serializable> Grid<T> createByCellSize(final Size cellSize, final Bounds bounds, final Class<T> type) {
        final var size = Size.of(
            Math.ceil(bounds.width() / cellSize.width()),
            Math.ceil(bounds.height() / cellSize.height()));
        return createByGridSize(size, bounds, type);
    }

    /**
     * Creates a new instance with the specified grid size.
     */
    public static <T extends Serializable> Grid<T> createByGridSize(final Size size, final Bounds bounds, final Class<T> type) {
        return new Grid<T>(size, bounds, type);
    }

    public Grid(final Size size, final Bounds bounds, final Class<T> type) {
        Validate.isTrue(size::isValid, "grid size must be valid");
        this.size = size;
        this.cellData = (T[]) Array.newInstance(type, size.width() * size.height());
        updateBounds(bounds);
    }

    /**
     * Fills all cells with the specified data.
     *
     * @since 3.33.0
     */
    public void fill(T value) {
        Arrays.fill(cellData, value);
    }

    /**
     * Fills all cells with data provided by the specified {@link Supplier}.
     *
     * @since 3.33.0
     */
    public void fill(final Supplier<T> value) {
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
        final double x = (cell.x() + 0.5) * cellWidth + bounds.origin().x();
        final double y = (cell.y() + 0.5) * cellHeight + bounds.origin().y();
        return Vector.$(x, y);
    }

    /**
     * Returns the cell {@link Bounds} of the specified cell within the world. Will return {@link Bounds} that reside outside the grid.
     */
    public Bounds cellBounds(final Offset cell) {
        final Vector position = cellPosition(cell);
        return Bounds.atPosition(position, cellWidth, cellHeight);
    }

    /**
     * Returns the cell at the specified positon. Will return cells that reside outside the grid.
     */
    public Offset toCell(final Vector position) {
        final var translated = position.subtract(bounds.origin());
        return Offset.at(toCellX(translated.x()), toCellY(translated.y()));
    }

    /**
     * Returns a list of all cells within the grid.
     */
    public List<Offset> cells() {
        final var nodes = new ArrayList<Offset>();
        for (int x = 0; x < size.width(); x++) {
            for (int y = 0; y < size.height(); y++) {
                nodes.add(Offset.at(x, y));
            }
        }
        return nodes;
    }

    public Size size() {
        return size;
    }

    /**
     * Returns the number of cells within the grid.
     */
    public int cellCount() {
        return size.pixelCount();
    }

    /**
     * Returns the width of the cells.
     */
    public double cellWidth() {
        return cellWidth;
    }

    /**
     * Returns the height of the cells.
     */
    public double cellHeight() {
        return cellHeight;
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
        return x >= 0 && x < size.width() && y >= 0 && y < size.height();
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
        validateGridPosition(x, y);
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
        validateGridPosition(x, y);
        final var internalIndex = toDataIndex(x, y);
        return cellData[internalIndex];
    }

    /**
     * Sets the contents of all cells within the specified area.
     */
    public void fill(final Bounds area, final T value) {
        final var areaTranslated = area.moveBy(-this.bounds.origin().x(), -this.bounds.origin().y()).expand(-0.1);
        final int minX = Math.max(toCellX(areaTranslated.origin().x()), 0);
        final int maxX = Math.min(toCellX(areaTranslated.bottomRight().x()), size.width() - 1);
        final int minY = Math.max(toCellY(areaTranslated.origin().y()), 0);
        final int maxY = Math.min(toCellY(areaTranslated.bottomRight().y()), size.height() - 1);
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

    /**
     * Reassignes the {@link Grid#bounds()}. May affect {@link #cellWidth()} and {@link #cellHeight()}.
     *
     * @since 3.33.0
     */
    public void updateBounds(final Bounds bounds) {
        requireNonNull(bounds, "grid bounds must not be null");

        this.bounds = bounds;
        this.cellWidth = bounds.width() / this.size.width();
        this.cellHeight = bounds.height() / this.size.height();
    }

    private int toCellX(final double value) {
        return (int) Math.floor(value / cellWidth);
    }

    private int toCellY(final double value) {
        return (int) Math.floor(value / cellHeight);
    }

    private int toDataIndex(final int x, final int y) {
        return y * size.width() + x;
    }

    private void validateGridPosition(final int x, final int y) {
        if (!contains(x, y)) {
            throw new IllegalArgumentException("position is not within grid: " + Offset.at(x, y));
        }
    }
}
