package de.suzufa.screwbox.tiled.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.ObjectDictionary;

//TODO: Generic type like TileDictionary
public class DefaultObjectDictionary implements ObjectDictionary {

    private final List<GameObject> objects = new ArrayList<>();

    @Override
    public List<GameObject> allObjects() {
        return objects;
    }

    public void add(final GameObject object) {
        objects.add(object);
    }

    @Override
    public Optional<GameObject> findByName(final String name) {
        return objects.stream()
                .filter(o -> name.equals(o.name()))
                .findFirst();
    }

    @Override
    public List<GameObject> findAllWithName(final String name) {
        return objects.stream()
                .filter(o -> name.equals(o.name()))
                .toList();
    }

    @Override
    public Optional<GameObject> findById(final int id) {
        return objects.stream()
                .filter(o -> id == o.id())
                .findFirst();
    }

}
