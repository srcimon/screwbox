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

    default List<Layer> allLayers() {
        return buildLayerDictionary().allLayers();
    }

    default List<GameObject> allObjects() {
        return buildObjectDictionary().allObjects();
    }

    default List<Tile> allTiles() {
        return buildTileDictionary().allTiles();
    }

    @Deprecated
    public default List<Extractor<Map, ?>> allExtractors() {
        return List.of(layerExtractor(), tileExtractor(), objectExtractor(), mapExtractor());
    }

    @Deprecated
    public default Extractor<Map, Layer> layerExtractor() {
        return input -> buildLayerDictionary().allLayers();
    }

    @Deprecated
    public default Extractor<Map, Tile> tileExtractor() {
        return input -> buildTileDictionary().allTiles();
    }

    @Deprecated
    public default Extractor<Map, GameObject> objectExtractor() {
        return input -> buildObjectDictionary().allObjects();
    }

    @Deprecated
    public default Extractor<Map, Map> mapExtractor() {
        return List::of;
    }
}
