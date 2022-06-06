package de.suzufa.screwbox.tiled;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;

public interface Map {

    Bounds bounds();

    TileDicitonary buildTileDictionary();

    ObjectDictionary buildObjectDictionary();

    LayerDictionary buildLayerDictionary();

    Properties properties();

    default List<Layer> allLayers() {
        return buildLayerDictionary().allLayers();
    }

    default List<GameObject> allObjects() {
        return buildObjectDictionary().allObjects();
    }

    default List<Tile> allTiles() {
        return buildTileDictionary().allTiles();
    }

}
