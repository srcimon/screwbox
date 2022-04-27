package de.suzufa.screwbox.tiled;

import java.util.List;

public interface TileDicitonary {

    List<Tile> allTiles();

    List<Tile> allFromLayer(String name);

    void add(Tile tile);

}