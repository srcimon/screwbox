package de.suzufa.screwbox.core.entityengine.internal;

import static java.util.Objects.isNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntityEngine;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.SourceImport;

public class DefaultEntityEngine implements EntityEngine {

    private final EntityManager entityManager;
    private final SystemManager systemManager;

    public DefaultEntityEngine(final EntityManager entityManager, final SystemManager systemManager) {
        this.entityManager = entityManager;
        this.systemManager = systemManager;
    }

    @Override
    public EntityEngine add(final Entity entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        entityManager.addEntity(entity);
        return this;
    }

    @Override
    public EntityEngine add(final EntitySystem system) {
        Objects.requireNonNull(system, "system must not be null");
        systemManager.addSystem(system);
        return this;
    }

    public void update() {
        systemManager.updateAllSystems();
    }

    @Override
    public List<Entity> fetchAll(final Archetype archetype) {
        return entityManager.entitiesMatching(archetype);
    }

    @Override
    public Entity forcedFetchSingle(final Archetype archetype) {
        final Optional<Entity> entity = fetchSingle(archetype);
        if (entity.isEmpty()) {
            throw new IllegalStateException("didn't find exactly one entity matching " + archetype);
        }
        return entity.get();
    }

    @Override
    public Optional<Entity> fetchSingle(final Archetype archetype) {
        final var entities = entityManager.entitiesMatching(archetype);
        if (entities.size() == 1) {
            return Optional.of(entities.get(0));
        }
        return Optional.empty();
    }

    @Override
    public void remove(final Entity entity) {
        entityManager.removeEntity(entity);
    }

    @Override
    public long entityCount() {
        return entityManager.allEntities().size();
    }

    @Override
    public boolean contains(final Archetype archetype) {
        return !entityManager.entitiesMatching(archetype).isEmpty();
    }

    @Override
    public EntityEngine add(final EntitySystem... systems) {
        for (final var system : systems) {
            add(system);
        }
        return this;
    }

    @Override
    public EntityEngine add(final Entity... entities) {
        for (final var entity : entities) {
            add(entity);
        }
        return this;
    }

    public List<EntitySystem> getSystems() {
        return systemManager.getSystems();
    }

    public void updateTimes(final int count) {
        for (int iteration = 1; iteration <= count; iteration++) {
            update();
        }
    }

    @Override
    public boolean isSystemPresent(final Class<? extends EntitySystem> type) {
        return systemManager.isSystemPresent(type);
    }

    @Override
    public void remove(final Class<? extends EntitySystem> systemType) {
        systemManager.removeSystem(systemType);
    }

    @Override
    public Entity forcedFetchById(final int id) {
        final Entity entity = entityManager.findById(id);
        if (isNull(entity)) {
            throw new IllegalArgumentException("could not find entity with id " + id);
        }
        return entity;
    }

    @Override
    public Optional<Entity> fetchById(final int id) {
        final Entity entity = entityManager.findById(id);
        return Optional.ofNullable(entity);
    }

    @Override
    public EntityEngine add(final List<Entity> entities) {
        for (final var entity : entities) {
            add(entity);
        }
        return this;
    }

    @Override
    public List<Entity> allEntities() {
        return entityManager.allEntities();
    }

    @Override
    public <T> SourceImport<T> importSource(T source) {
        Objects.requireNonNull(source, "Source must not be null");
        return importSource(List.of(source));
    }

    @Override
    public <T> SourceImport<T> importSource(List<T> source) {
        Objects.requireNonNull(source, "Source must not be null");
        return new SourceImport<>(source, this);
    }
}
