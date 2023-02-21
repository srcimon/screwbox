package io.github.simonbas.screwbox.tiled;

import io.github.simonbas.screwbox.tiled.internal.LayerEntity;
import io.github.simonbas.screwbox.tiled.internal.MapEntity;
import io.github.simonbas.screwbox.tiled.internal.ObjectEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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