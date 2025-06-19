package dev.screwbox.core.creation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class TileMap<T> {

    protected final List<Tile<T>> tiles = new ArrayList<>();

    /**
     * Returns all {@link Tile tiles} contained in the map.
     */
    public List<Tile<T>> tiles() {
        return Collections.unmodifiableList(tiles);
    }
}
