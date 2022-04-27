package de.suzufa.screwbox.tiled.internal;

import java.util.List;

import de.suzufa.screwbox.tiled.Layer;
import de.suzufa.screwbox.tiled.LayerDictionary;

public class DefaultLayerDicitonary implements LayerDictionary {

    private final List<Layer> layers;

    public DefaultLayerDicitonary(final List<Layer> layers) {
        this.layers = layers;
    }

    @Override
    public List<Layer> allLayers() {
        return layers;
    }

}
