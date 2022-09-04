package de.suzufa.screwbox.tiled;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.suzufa.screwbox.tiled.internal.LayerEntity;
import de.suzufa.screwbox.tiled.internal.MapEntity;

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

    Optional<Layer> findByName(String name) {
        return layers.stream()
                .filter(l -> "name".equals(l.name()))
                .findFirst();
    }
}
