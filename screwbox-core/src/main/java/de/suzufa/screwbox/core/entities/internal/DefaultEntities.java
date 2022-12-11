package de.suzufa.screwbox.core.entities.internal;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entities;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.SourceImport;

public class DefaultEntities implements Entities {

    private final EntityManager entityManager;
    private final SystemManager systemManager;

    public DefaultEntities(final Engine engine) {
        this.entityManager = new EntityManager();
        this.systemManager = new SystemManager(engine, entityManager);
    }

    @Override
    public Entities add(final Entity entity) {
        requireNonNull(entity, "entity must not be null");
        entityManager.addEntity(entity);
        return this;
    }

    @Override
    public Entities add(final EntitySystem system) {
        requireNonNull(system, "system must not be null");
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
    public Entity forcedFetch(final Archetype archetype) {
        final Optional<Entity> entity = fetch(archetype);
        if (entity.isEmpty()) {
            throw new IllegalStateException("didn't find exactly one entity matching " + archetype);
        }
        return entity.get();
    }

    @Override
    public Optional<Entity> fetch(final Archetype archetype) {
        final var entities = entityManager.entitiesMatching(archetype);
        if (entities.size() == 1) {
            return Optional.of(entities.get(0));
        }
        return Optional.empty();
    }

    @Override
    public Entities remove(final Entity entity) {
        entityManager.removeEntity(entity);
        return this;
    }

    @Override
    public Entities remove(final List<Entity> entities) {
        for (final var entity : entities) {
            remove(entity);
        }
        return this;
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
    public Entities add(final EntitySystem... systems) {
        for (final var system : systems) {
            add(system);
        }
        return this;
    }

    @Override
    public Entities add(final Entity... entities) {
        for (final var entity : entities) {
            add(entity);
        }
        return this;
    }

    @Override
    public List<EntitySystem> systems() {
        return systemManager.allSystems();
    }

    public void updateTimes(final int count) {
        for (int iteration = 1; iteration <= count; iteration++) {
            update();
        }
    }

    @Override
    public Entities clearEntities() {
        for (final var entity : new ArrayList<>(allEntities())) {
            remove(entity);
        }
        return this;
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
    public Entities add(final List<Entity> entities) {
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
    public <T> SourceImport<T> importSource(final T source) {
        requireNonNull(source, "Source must not be null");
        return importSource(List.of(source));
    }

    @Override
    public <T> SourceImport<T> importSource(final List<T> source) {
        requireNonNull(source, "Source must not be null");
        return new SourceImport<>(source, this);
    }

    @Override
    public Entities toggleSystem(final EntitySystem entitySystem) {
        final Class<? extends EntitySystem> systemClass = entitySystem.getClass();
        if (isSystemPresent(systemClass)) {
            remove(systemClass);
        } else {
            add(entitySystem);
        }
        return this;
    }

}
