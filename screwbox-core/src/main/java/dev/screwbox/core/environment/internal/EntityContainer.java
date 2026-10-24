package dev.screwbox.core.environment.internal;

import dev.screwbox.core.environment.Entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Allows fast adding and removing of {@link Entity entities} but will also reduce overhead of retrieving all
 * {@link Entity entities} as list.
 */
class EntityContainer {

    private final Set<Entity> entitiesSet = new HashSet<>();
    private List<Entity> entities = new ArrayList<>();
    private boolean isOutOfSync = false;

    public EntityContainer() {
    }

    public EntityContainer(final List<Entity> entities) {
        entitiesSet.addAll(entities);
        isOutOfSync = true;
    }


    public void add(final Entity entity) {
        isOutOfSync = entitiesSet.add(entity);
    }

    public List<Entity> all() {
        if (isOutOfSync) {
            entities = new ArrayList<>(entitiesSet);
            isOutOfSync = false;
        }
        return entities;
    }

    public boolean contains(final Entity entity) {
        return entitiesSet.contains(entity);
    }

    public void remove(final Entity entity) {
        isOutOfSync = entitiesSet.remove(entity);
    }
}
