package de.suzufa.screwbox.tiled;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.tiled.internal.DefaultLayer;
import de.suzufa.screwbox.tiled.internal.entity.LayerEntity;
import de.suzufa.screwbox.tiled.internal.entity.MapEntity;

public class LayerDictionary {

    private final List<Layer> layers = new ArrayList<>();

    public LayerDictionary(MapEntity mapEntity) {
        int order = 0;
        for (LayerEntity layerEntity : mapEntity.getLayers()) {
            layers.add(new DefaultLayer(layerEntity, order));
            order++;
        }
    }

    public List<Layer> allLayers() {
        return layers;
    }
}
