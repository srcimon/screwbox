package de.suzufa.screwbox.core.entityengine;

import java.util.List;
import java.util.Optional;

import de.suzufa.screwbox.core.entityengine.SourceImport.Converter;

public interface EntityEngine {

    EntityEngine add(Entity entity);

    EntityEngine add(List<Entity> entities);

    EntityEngine add(EntitySystem system);

    EntityEngine add(EntitySystem... systems);

    List<Entity> fetchAll(Archetype archetype);

    default List<Entity> fetchAllHaving(Class<? extends Component> component) {
        return fetchAll(Archetype.of(component));
    }

    default List<Entity> fetchAllHaving(Class<? extends Component> componentA, Class<? extends Component> componentB) {
        return fetchAll(Archetype.of(componentA, componentB));
    }

    Optional<Entity> fetch(Archetype archetype);

    default Optional<Entity> fetchHaving(Class<? extends Component> component) {
        return fetch(Archetype.of(component));
    }

    default Optional<Entity> fetchHaving(Class<? extends Component> componentA, Class<? extends Component> componentB) {
        return fetch(Archetype.of(componentA, componentB));
    }

    Entity forcedFetch(Archetype archetype);

    default Entity forcedFetchHaving(Class<? extends Component> component) {
        return forcedFetch(Archetype.of(component));
    }

    default Entity forcedFetchHaving(Class<? extends Component> componentA, Class<? extends Component> componentB) {
        return forcedFetch(Archetype.of(componentA, componentB));
    }

    Entity forcedFetchById(int id);

    Optional<Entity> fetchById(int id);

    void remove(Entity entity);

    void remove(Class<? extends EntitySystem> systemType);

    long entityCount();

    boolean contains(Archetype archetype);

    EntityEngine add(Entity... entities);

    boolean isSystemPresent(Class<? extends EntitySystem> type);

    List<Entity> allEntities();

    /**
     * Provides a compact syntax for importing {@link Entity}s from a custom source
     * using conditions and {@link Converter}.
     * 
     * @see #importSource(List) for multiple sources
     */
    <T> SourceImport<T> importSource(T source);

    /**
     * Provides a compact syntax for importing {@link Entity}s from multiple custom
     * sources using conditions and {@link Converter}.
     * 
     * @see #importSource(Object) for single source
     */
    <T> SourceImport<T> importSource(List<T> source);
}