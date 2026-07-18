package dev.screwbox.core;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.navigation.BinaryGrid;
import dev.screwbox.core.utils.Validate;

import java.io.Serial;
import java.io.Serializable;
import java.util.BitSet;

import static java.util.Objects.requireNonNull;

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

    public Grid(final Bounds bounds, final int cellSize) {
        requireNonNull(bounds, "grid bounds must not be null");
        Validate.positive(cellSize, "cell size must be positive");
        Validate.isTrue(() -> bounds.origin().x() % cellSize == 0, "bounds should fit cell size");
        Validate.isTrue(() -> bounds.origin().y() % cellSize == 0, "bounds should fit cell size");

        this.cellSize = cellSize;
        this.bounds = bounds;
        width = toGrid(bounds.width());
        height = toGrid(bounds.height());
    }

    /**
     * Returns the area of this {@link BinaryGrid} in the {@link World}.
     */
    public final Bounds bounds() {
        return bounds;
    }

    protected int toGrid(final double value) {
        return Math.floorDiv((int) value, cellSize);
    }
}
