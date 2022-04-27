package de.suzufa.screwbox.tiled.internal;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.tiled.Layer;
import de.suzufa.screwbox.tiled.LayerDictionary;
import de.suzufa.screwbox.tiled.internal.entity.LayerEntity;
import de.suzufa.screwbox.tiled.internal.entity.MapEntity;

public class LayerDictionaryBuilder {

    public LayerDictionary buildDictionary(MapEntity mapEntity) {
        List<Layer> layers = new ArrayList<>();
        int order = 0;

        for (LayerEntity layerEntity : mapEntity.getLayers()) {
            layers.add(new DefaultLayer(layerEntity, order));
            order++;
        }
        return new DefaultLayerDicitonary(layers);
    }

}
