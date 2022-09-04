package de.suzufa.screwbox.tiled;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.suzufa.screwbox.tiled.internal.LayerEntity;
import de.suzufa.screwbox.tiled.internal.MapEntity;
import de.suzufa.screwbox.tiled.internal.ObjectEntity;

class GameObjectCollection {

    private final List<GameObject> objects = new ArrayList<>();

    GameObjectCollection(final MapEntity map) {
        int order = 0;
        for (final LayerEntity layerEntity : map.getLayers()) {
            final Layer layer = new Layer(layerEntity, order);
            for (final ObjectEntity object : layerEntity.objects()) {
                final GameObject tiledObject = new GameObject(object, layer);
                objects.add(tiledObject);
            }
            order++;
        }
    }

    List<GameObject> all() {
        return objects;
    }

    Optional<GameObject> findByName(final String name) {
        return objects.stream()
                .filter(o -> name.equals(o.name()))
                .findFirst();
    }
}