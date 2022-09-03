package de.suzufa.screwbox.tiled;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.suzufa.screwbox.tiled.internal.entity.LayerEntity;
import de.suzufa.screwbox.tiled.internal.entity.MapEntity;

public class LayersCollection {

    private final List<Layer> layers = new ArrayList<>();

    LayersCollection(final MapEntity mapEntity) {
        int order = 0;
        for (final LayerEntity layerEntity : mapEntity.layers()) {
            layers.add(new Layer(layerEntity, order));
            order++;
        }
    }

    public List<Layer> all() {
        return layers;
    }

    public Optional<Layer> findByName(String name) {
        return layers.stream()
                .filter(l -> "name".equals(l.name()))
                .findFirst();
    }
}
