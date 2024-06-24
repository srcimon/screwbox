package io.github.srcimon.screwbox.tiled;

import io.github.srcimon.screwbox.tiled.internal.LayerEntity;
import io.github.srcimon.screwbox.tiled.internal.MapEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class GameObjectCollection {

    private final List<GameObject> objects = new ArrayList<>();

    GameObjectCollection(final MapEntity map) {
        int order = 0;
        for (final LayerEntity layerEntity : map.getLayers()) {
            final Layer layer = new Layer(layerEntity, order);
            layerEntity.objects().stream()
                    .map(objectEntity -> new GameObject(objectEntity, layer))
                    .forEach(objects::add);
            order++;
        }
    }

    List<GameObject> all() {
        return objects;
    }

    Optional<GameObject> findByName(final String name) {
        return objects.stream()
                .filter(object -> name.equals(object.name()))
                .findFirst();
    }
}