package dev.screwbox.core.environment.internal;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.BlueprintImport;
import dev.screwbox.core.environment.BlueprintImportOptions;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.utils.Reflections;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class DefaultEnvironment implements Environment {

    private final EntityManager entityManager = new EntityManager();
    private final SavegameManager savegameManager = new SavegameManager();
    private final SystemManager systemManager;
    private int idIndex = Integer.MIN_VALUE;

    public DefaultEnvironment(final Engine engine) {
        this.systemManager = new SystemManager(engine, entityManager);
    }

    @Override
    public <T extends Component> Optional<T> tryFetchSingletonComponent(final Class<T> component) {
        return tryFetchSingleton(component).map(entity -> entity.get(component));
    }

    @Override
    public Optional<Entity> tryFetchSingleton(final Class<? extends Component> component) {
        return fetchAllOfArchetype(Archetype.of(component), component.getSimpleName());
    }

    @Override
    public Optional<Entity> tryFetchSingleton(final Archetype archetype) {
        return fetchAllOfArchetype(archetype, archetype.toString());
    }

    private Optional<Entity> fetchAllOfArchetype(Archetype archetype, String searchItem) {
        final var entities = fetchAll(archetype);
        if (entities.size() > 1) {
            throw new IllegalStateException("singleton has been found multiple times: " + searchItem);
        }
        return entities.size() == 1
                ? Optional.of(entities.getFirst())
                : Optional.empty();
    }

    @Override
    public boolean hasSingleton(final Class<? extends Component> component) {
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
    public Environment addSystem(final Order order, final EntitySystem system) {
        requireNonNull(order, "order must not be null");
        verifySystemNotNull(system);
        systemManager.addSystem(system, order);
        return this;
    }

    @Override
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
    public long entityCount(final Archetype archetype) {
        return entityManager.entitiesMatching(archetype).size();
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
    public Environment removeAllComponentsOfType(final Class<? extends Component> componentType) {
        for (final var entity : fetchAllHaving(componentType)) {
            entity.remove(componentType);
        }
        return this;
    }

    @Override
    public void remove(final Class<? extends EntitySystem> systemType) {
        systemManager.removeSystem(systemType);
    }

    @Override
    public Entity fetchById(final int id) {
        final Entity entity = entityManager.findById(id);
        if (isNull(entity)) {
            throw new IllegalArgumentException("could not find entity with id " + id);
        }
        return entity;
    }

    @Override
    public Optional<Entity> tryFetchById(final int id) {
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
    public <T, I> void importEntities(List<T> source, BlueprintImportOptions<T, I> options) {
        new BlueprintImport<>(this, source, options).run();
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
    public Environment saveToFile(final String name) {
        savegameManager.createSavegame(entities(), name);
        return this;
    }

    @Override
    public Environment loadFromFile(final String name) {
        clearEntities();
        addEntities(savegameManager.loadSavegame(name));
        return this;
    }

    @Override
    public Environment deleteSavegameFile(final String name) {
        savegameManager.deleteSavegame(name);
        return this;
    }

    @Override
    public boolean savegameFileExists(String name) {
        return savegameManager.savegameExists(name);
    }

    @Override
    public Environment enablePhysics() {
        return enableFeature(Feature.PHYSICS);
    }

    @Override
    public Environment enableNavigation() {
        return enableFeature(Feature.NAVIGATION);
    }

    @Override
    public Environment enableControls() {
        return enableFeature(Feature.CONTROLS);
    }

    @Override
    public Environment enableRendering() {
        return enableFeature(Feature.RENDERING);
    }

    @Override
    public Environment enableFluids() {
        return enableFeature(Feature.FLUIDS);
    }

    @Override
    public Environment enableAi() {
        return enableFeature(Feature.AI);
    }

    @Override
    public Environment enableTweening() {
        return enableFeature(Feature.TWEENING);
    }

    @Override
    public Environment enableLogic() {
        return enableFeature(Feature.LOGIC);
    }

    @Override
    public Environment enableLight() {
        return enableFeature(Feature.LIGHT);
    }

    @Override
    public Environment enableSoftPhysics() {
        return enableFeature(Feature.SOFT_PHYSICS);
    }

    @Override
    public Environment enableParticles() {
        return enableFeature(Feature.PARTICLES);
    }

    @Override
    public Environment enableAudio() {
        return enableFeature(Feature.AUDIO);
    }

    @Override
    public Environment enableAllFeatures() {
        for (final var feature : Feature.values()) {
            enableFeature(feature);
        }
        return this;
    }

    @Override
    public Environment addSystemsFromPackage(final String packageName) {
        requireNonNull(packageName, "package name must not be null");
        final var systems = Reflections.createInstancesFromPackage(packageName, EntitySystem.class);
        Validate.notEmpty(systems, "could not add any entity system from package: " + packageName);
        systems.forEach(this::addSystem);
        return this;
    }

    @Override
    public int currentDrawOrder() {
        return systemManager.currentDrawOrder();
    }

    @Override
    public int allocateId() {
        idIndex++;
        ensureIdIsUniqueAndWithinValidRange();
        return idIndex;
    }

    @Override
    public int peekId() {
        ensureIdIsUniqueAndWithinValidRange();
        return idIndex + 1;
    }

    private void ensureIdIsUniqueAndWithinValidRange() {
        while (entityManager.idIsPresent(idIndex)) {
            idIndex++;
            if (idIndex >= 0) {
                idIndex = Integer.MIN_VALUE;
            }
        }
    }

    private Environment enableFeature(final Feature feature) {
        for (final var system : feature.systems) {
            addOrReplaceSystem(system);
        }
        return this;
    }

    private void verifySystemNotNull(EntitySystem system) {
        requireNonNull(system, "system must not be null");
    }
}
