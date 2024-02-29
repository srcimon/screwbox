package io.github.srcimon.screwbox.core.environment.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class DefaultEnvironment implements Environment {

    private final EntityManager entityManager = new EntityManager();
    private final SavegameManager savegameManager = new SavegameManager();
    private final SystemManager systemManager;

    public DefaultEnvironment(final Engine engine) {
        this.systemManager = new SystemManager(engine, entityManager);
    }

    @Override
    public <T extends SingletonComponent> Optional<T> tryFetchSingletonComponent(final Class<T> component) {
        return tryFetchSingleton(component).map(entity -> entity.get(component));
    }

    @Override
    public Optional<Entity> tryFetchSingleton(final Class<? extends SingletonComponent> component) {
        final var entities = fetchAllHaving(component);
        if (entities.size() > 1) {
            throw new IllegalStateException("singleton component has been found multiple times: " + component.getSimpleName());
        }
        return entities.size() == 1
                ? Optional.of(entities.getFirst())
                : Optional.empty();
    }

    @Override
    public boolean hasSingleton(final Class<? extends SingletonComponent> component) {
        return tryFetchSingleton(component).isPresent();
    }

    @Override
    public Environment addEntity(final String name, final Component... components) {
        return addEntity(new Entity().name(name).add(components));
    }

    @Override
    public Environment addEntity(final int id, final Component... components) {
        return addEntity(new Entity(id).add(components));
    }

    @Override
    public Environment addEntity(final int id, final String name, final Component... components) {
        return addEntity(new Entity(id).name(name).add(components));
    }

    @Override
    public Environment addEntity(final Component... components) {
        return addEntity(new Entity().add(components));
    }

    @Override
    public Environment addEntity(final Entity entity) {
        requireNonNull(entity, "entity must not be null");
        entityManager.addEntity(entity);
        return this;
    }

    @Override
    public Environment addSystem(final EntitySystem system) {
        verifySystemNotNull(system);
        systemManager.addSystem(system);
        return this;
    }

    @Override
    public Environment addSystem(final SystemOrder order, final EntitySystem system) {
        requireNonNull(order, "order must not be null");
        verifySystemNotNull(system);
        systemManager.addSystem(system, order);
        return this;
    }

    public Environment addOrReplaceSystem(final EntitySystem system) {
        verifySystemNotNull(system);
        final var systemClass = system.getClass();
        if (isSystemPresent(systemClass)) {
            remove(systemClass);
        }
        addSystem(system);
        return this;
    }

    @Override
    public Environment removeSystemIfPresent(final Class<? extends EntitySystem> systemType) {
        if (isSystemPresent(systemType)) {
            remove(systemType);
        }
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
            return Optional.of(entities.getFirst());
        }
        return Optional.empty();
    }

    @Override
    public Environment remove(final Entity entity) {
        entityManager.removeEntity(entity);
        return this;
    }

    @Override
    public Environment remove(final List<Entity> entities) {
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
    public Environment addSystems(final EntitySystem... systems) {
        for (final var system : systems) {
            addSystem(system);
        }
        return this;
    }

    @Override
    public Environment addEntities(final Entity... entities) {
        for (final var entity : entities) {
            addEntity(entity);
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
    public Environment clearEntities() {
        for (final var entity : new ArrayList<>(entities())) {
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
    public Environment addEntities(final List<Entity> entities) {
        for (final var entity : entities) {
            addEntity(entity);
        }
        return this;
    }

    @Override
    public List<Entity> entities() {
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
    public Environment toggleSystem(final EntitySystem entitySystem) {
        final Class<? extends EntitySystem> systemClass = entitySystem.getClass();
        if (isSystemPresent(systemClass)) {
            remove(systemClass);
        } else {
            addSystem(entitySystem);
        }
        return this;
    }


    @Override
    public Environment createSavegame(final String name) {
        savegameManager.createSavegame(entities(), name);
        return this;
    }

    @Override
    public Environment loadSavegame(final String name) {
        clearEntities();
        addEntities(savegameManager.loadSavegame(name));
        return this;
    }

    @Override
    public Environment deleteSavegame(final String name) {
        savegameManager.deleteSavegame(name);
        return this;

    }

    @Override
    public boolean savegameExists(String name) {
        return savegameManager.savegameExists(name);
    }

    @Override
    public Environment enablePhysics() {
        enableFeature(Feature.PHYSICS);
        return this;
    }

    @Override
    public Environment enableRendering() {
        enableFeature(Feature.RENDERING);
        return this;
    }

    @Override
    public Environment enableTweening() {
        enableFeature(Feature.TWEENING);
        return this;
    }

    @Override
    public Environment enableLogic() {
        enableFeature(Feature.LOGIC);
        return this;
    }

    @Override
    public Environment enableLight() {
        enableFeature(Feature.LIGHT);
        return this;
    }

    private void enableFeature(final Feature feature) {
        for (final var system : feature.systems) {
            addOrReplaceSystem(system);
        }
    }

    private void verifySystemNotNull(EntitySystem system) {
        requireNonNull(system, "system must not be null");
    }
}
