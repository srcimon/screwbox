package de.suzufa.screwbox.tiled;

import de.suzufa.screwbox.core.Bounds;

public interface Map {

    Bounds bounds();

    TileDicitonary buildTileDictionary();

    ObjectDictionary buildObjectDictionary();

    LayerDictionary buildLayerDictionary();

    Properties properties();
}
