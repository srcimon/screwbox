package de.suzufa.screwbox.tiled.internal;

import de.suzufa.screwbox.tiled.Layer;
import de.suzufa.screwbox.tiled.ObjectDictionary;
import de.suzufa.screwbox.tiled.internal.entity.LayerEntity;
import de.suzufa.screwbox.tiled.internal.entity.MapEntity;
import de.suzufa.screwbox.tiled.internal.entity.ObjectEntity;

//TODO: Test
public class ObjectDictionaryBuilder {

    public ObjectDictionary buildDictionary(MapEntity map) {
        DefaultObjectDictionary dictionary = new DefaultObjectDictionary();

        int order = 0;
        for (LayerEntity layerEntity : map.getLayers()) {
            Layer layer = new DefaultLayer(layerEntity, order);
            for (ObjectEntity object : layerEntity.getObjects()) {
                DefaultGameObject tiledObject = new DefaultGameObject(object, layer);
                dictionary.add(tiledObject);
            }
            order++;
        }

        return dictionary;
    }

}
