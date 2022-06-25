package de.suzufa.screwbox.core.entityengine.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entityengine.EntitySystem;

public class DefaultSystemManager implements SystemManager {

    private final List<EntitySystem> systems = new ArrayList<>();
    private final EntityManager entityManager;
    private final Engine engine;
    private boolean delayChanges = false;

    private final List<EntitySystem> pendingSystemsToAdd = new ArrayList<>();
    private final List<Class<? extends EntitySystem>> pendingSystemsToRemove = new ArrayList<>();

    public DefaultSystemManager(final Engine engine, final EntityManager entityManager) {
        this.engine = engine;
        this.entityManager = entityManager;
    }

    @Override
    public void addSystem(final EntitySystem system) {
        if (delayChanges) {
            pendingSystemsToAdd.add(system);
        } else {
            systems.add(system);
            Collections.sort(systems, new SystemComparator());
        }
    }

    @Override
    public List<EntitySystem> allSystems() {
        return systems;
    }

    @Override
    public void updateAllSystems() {
        entityManager.pickUpChanges();
        delayChanges();
        for (final EntitySystem entitySystem : systems) {
            entityManager.delayChanges();
            entitySystem.update(engine);
            entityManager.pickUpChanges();
        }
        pickUpChanges();
        entityManager.delayChanges();
    }

    private void delayChanges() {
        delayChanges = true;
    }

    private void pickUpChanges() {
        delayChanges = false;

        for (final var system : pendingSystemsToAdd) {
            addSystem(system);
        }
        pendingSystemsToAdd.clear();

        for (final var systemType : pendingSystemsToRemove) {
            removeSystem(systemType);
        }

        pendingSystemsToRemove.clear();
    }

    @Override
    public void removeSystem(final Class<? extends EntitySystem> systemType) {
        if (delayChanges) {
            pendingSystemsToRemove.add(systemType);
        } else {
            for (final var system : systemsOfType(systemType)) {
                systems.remove(system);
            }
        }
    }

    private List<EntitySystem> systemsOfType(Class<? extends EntitySystem> type) {
        final List<EntitySystem> systemsOfType = new ArrayList<>();
        for (var system : systems) {
            if (type == system.getClass()) {
                systemsOfType.add(system);
            }
        }
        return systemsOfType;
    }

    @Override
    public boolean isSystemPresent(Class<? extends EntitySystem> type) {
        for (final var system : systems) {
            if (type == system.getClass()) {
                return true;
            }
        }
        return false;
    }

}
