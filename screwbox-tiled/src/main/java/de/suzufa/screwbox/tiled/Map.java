package de.suzufa.screwbox.tiled;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;

public interface Map {

    Bounds bounds();

    TileDicitonary buildTileDictionary();

    ObjectDictionary buildObjectDictionary();

    LayerDictionary buildLayerDictionary();

    Properties properties();

    public default Extractor<Map, Layer> layerExtractor() {
        return input -> buildLayerDictionary().allLayers();
    }

    public default Extractor<Map, Tile> tileExtractor() {
        return input -> buildTileDictionary().allTiles();
    }

    public default Extractor<Map, GameObject> objectExtractor() {
        return input -> buildObjectDictionary().allObjects();
    }

    public default Extractor<Map, Map> mapExtractor() {
        return input -> List.of(input);
    }
}
