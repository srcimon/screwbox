package io.github.srcimon.screwbox.core.environment.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.*;
import io.github.srcimon.screwbox.core.utils.TimeoutCache;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class DefaultEnvironment implements Environment {

    private final TimeoutCache<String, Boolean> savegameCache = new TimeoutCache<>(ofSeconds(1));

    private final EntityManager entityManager;
    private final SystemManager systemManager;

    public DefaultEnvironment(final Engine engine) {
        this.entityManager = new EntityManager();
        this.systemManager = new SystemManager(engine, entityManager);
    }

    @Override
    public Environment addEntity(final int id, final Component... components) {
        return addEntity(new Entity(id).add(components));
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
    public Environment addSystem(final Entity... entities) {
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
        requireNonNull(name, "name must not be null");
        try (final var outputStream = new FileOutputStream(name)) {
            try (final var zippedOutputStream = new GZIPOutputStream(outputStream)) {
                try (final var objectOutputStream = new ObjectOutputStream(zippedOutputStream)) {
                    objectOutputStream.writeObject(entities());
                }
            }
        } catch (final IOException e) {
            throw new IllegalStateException("could not create savegame: " + name, e);
        }
        return this;
    }

    @Override
    public Environment loadSavegame(final String name) {
        requireNonNull(name, "name must not be null");
        clearEntities();
        try (final var inputStream = new FileInputStream(name)) {
            try (final var zippedInputStream = new GZIPInputStream(inputStream)) {
                try (final var objectInputStream = new ObjectInputStream(zippedInputStream)) {
                    final var allEntities = (List<Entity>) objectInputStream.readObject();
                    addEntities(allEntities);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("could not load savegame: " + name, e);
        }
        return this;
    }

    @Override
    public Environment deleteSavegame(final String name) {
        try {
            requireNonNull(name, "name must not be null");
            final Path path = Path.of(name);
            Files.delete(path);
            savegameCache.clear(name);
        } catch (IOException e) {
            throw new IllegalStateException("could not delete savegame: " + name, e);
        }
        return this;

    }

    @Override
    public boolean savegameExists(String name) {
        requireNonNull(name, "name must not be null");
        return savegameCache.getOrElse(name, () -> Files.exists(Path.of(name)));
    }

}
