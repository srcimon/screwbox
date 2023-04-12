package io.github.srcimon.screwbox.tiled;

import io.github.srcimon.screwbox.tiled.internal.LayerEntity;
import io.github.srcimon.screwbox.tiled.internal.MapEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class LayerCollection {

    private final List<Layer> layers = new ArrayList<>();

    LayerCollection(final MapEntity mapEntity) {
        int order = 0;
        for (final LayerEntity layerEntity : mapEntity.getLayers()) {
            layers.add(new Layer(layerEntity, order));
            order++;
        }
    }

    List<Layer> all() {
        return layers;
    }

    Optional<Layer> findByName(final String name) {
        return layers.stream()
                .filter(l -> name.equals(l.name()))
                .findFirst();
    }
}
