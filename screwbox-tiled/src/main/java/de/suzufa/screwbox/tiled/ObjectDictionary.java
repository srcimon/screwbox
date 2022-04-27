package de.suzufa.screwbox.tiled;

import java.util.List;
import java.util.Optional;

public interface ObjectDictionary {

    Optional<GameObject> findByName(String name);

    Optional<GameObject> findById(int id);

    List<GameObject> allObjects();

    List<GameObject> findAllWithName(String name);

}