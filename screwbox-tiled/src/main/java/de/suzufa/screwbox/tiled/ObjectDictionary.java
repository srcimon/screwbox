package de.suzufa.screwbox.tiled;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.suzufa.screwbox.tiled.internal.DefaultGameObject;
import de.suzufa.screwbox.tiled.internal.entity.LayerEntity;
import de.suzufa.screwbox.tiled.internal.entity.MapEntity;
import de.suzufa.screwbox.tiled.internal.entity.ObjectEntity;

//TODO: test
public class ObjectDictionary {

    private final List<GameObject> objects = new ArrayList<>();

    ObjectDictionary(final MapEntity map) {
        int order = 0;
        for (final LayerEntity layerEntity : map.getLayers()) {
            final Layer layer = new Layer(layerEntity, order);
            for (final ObjectEntity object : layerEntity.objects()) {
                final DefaultGameObject tiledObject = new DefaultGameObject(object, layer);
                objects.add(tiledObject);
            }
            order++;
        }
    }

    public List<GameObject> allObjects() {
        return objects;
    }

    public void add(final GameObject object) {
        objects.add(object);
    }

    public Optional<GameObject> findByName(final String name) {
        return objects.stream()
                .filter(o -> name.equals(o.name()))
                .findFirst();
    }

    public List<GameObject> findAllWithName(final String name) {
        return objects.stream()
                .filter(o -> name.equals(o.name()))
                .toList();
    }

    public Optional<GameObject> findById(final int id) {
        return objects.stream()
                .filter(o -> id == o.id())
                .findFirst();
    }

}