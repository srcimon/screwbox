package de.suzufa.screwbox.tiled.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.suzufa.screwbox.tiled.Tile;
import de.suzufa.screwbox.tiled.TileDicitonary;

public class DefaultTileDictionary implements TileDicitonary {

    private List<Tile> tiles = new ArrayList<>();

    @Override
    public void add(Tile tile) {
        tiles.add(tile);
    }

    @Override
    public List<Tile> allTiles() {
        return Collections.unmodifiableList(tiles);
    }

    @Override
    public List<Tile> allFromLayer(String name) {
        return tiles.stream()
                .filter(t -> name.equals(t.layer().name()))
                .toList();
    }
}
