package de.suzufa.screwbox.tiled;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.resources.Extractor;

public interface Map {

    Bounds bounds();

    TileDicitonary buildTileDictionary();

    ObjectDictionary buildObjectDictionary();

    LayerDictionary buildLayerDictionary();

    Properties properties();

    // TODO: REMOVE?
    public default List<Extractor<Map, ?>> allExtractors() {
        return List.of(layerExtractor(), tileExtractor(), objectExtractor(), mapExtractor());
    }

    // TODO: REMOVE?
    public default Extractor<Map, Layer> layerExtractor() {
        return input -> buildLayerDictionary().allLayers();
    }

    // TODO: REMOVE?
    public default Extractor<Map, Tile> tileExtractor() {
        return input -> buildTileDictionary().allTiles();
    }

    // TODO: REMOVE?
    public default Extractor<Map, GameObject> objectExtractor() {
        return input -> buildObjectDictionary().allObjects();
    }

    // TODO: REMOVE?
    public default Extractor<Map, Map> mapExtractor() {
        return List::of;
    }
}
