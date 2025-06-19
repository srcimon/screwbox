package dev.screwbox.core.creation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class TileMap<T> {

    protected final List<Tile<T>> tiles = new ArrayList<>();
    protected final int tileSize;
    protected int rows;
    protected int columns;

    //TODO make size -> Size
    protected TileMap(final int tileSize) {
        Validate.positive(tileSize, "tile size must be positive");
        this.tileSize =tileSize;
    }
    /**
     * Returns all {@link Tile tiles} contained in the map.
     */
    public List<Tile<T>> tiles() {
        return Collections.unmodifiableList(tiles);
    }

    /**
     * Returns the outer {@link Bounds} that contains all {@link #tiles()}.
     */
    public Bounds bounds() {
        return Bounds.atOrigin(0, 0, (double) tileSize * columns, (double) tileSize * rows);
    }
}
