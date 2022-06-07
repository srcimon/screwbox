package de.suzufa.screwbox.core.entityengine;

import java.util.List;
import java.util.Optional;

public interface EntityEngine {

    EntityEngine add(Entity entity);

    EntityEngine add(List<Entity> entities);

    EntityEngine add(EntitySystem system);

    EntityEngine add(EntitySystem... systems);

    List<Entity> fetchAll(Archetype archetype);

    Optional<Entity> fetchSingle(Archetype archetype);

    Entity forcedFetchSingle(Archetype archetype);

    Entity forcedFetchById(int id);

    Optional<Entity> fetchById(int id);

    void remove(Entity entity);

    void remove(Class<? extends EntitySystem> systemType);

    long entityCount();

    boolean contains(Archetype archetype);

    EntityEngine add(Entity... entities);

    boolean isSystemPresent(Class<? extends EntitySystem> type);

    List<Entity> allEntities();

    <T> SourceImport<T> importFromSource(T source);

    <T> SourceImport<T> importFromSource(List<T> source);
}