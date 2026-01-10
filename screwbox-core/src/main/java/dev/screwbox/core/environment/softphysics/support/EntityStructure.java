package dev.screwbox.core.environment.softphysics.support;

import dev.screwbox.core.environment.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

abstract class EntityStructure implements EntityCollection {
    private final Map<Entity, Set<String>> taggedEntities = new HashMap<>();
    private final List<Entity> entities = new ArrayList<>();

    protected void add(Entity entity) {
        taggedEntities.put(entity, new HashSet<>());
        this.entities.add(entity);
    }

    protected void tag(Entity entity, String tag) {
        taggedEntities.get(entity).add(tag);
    }

    protected List<Entity> entitiesWithTag(String tag) {
        return taggedEntities.entrySet().stream().filter(e -> taggedEntities.get(e.getKey()).contains(tag)).map(Map.Entry::getKey).toList();
    }

    @Override
    public Entity root() {
        return entities.getFirst();
    }

    public Entity last() {
        return entities.getLast();
    }

    @Override
    public List<Entity> all() {
        return Collections.unmodifiableList(entities);
    }
}
