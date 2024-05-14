package io.github.srcimon.screwbox.core.environment.internal;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.utils.Cache;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.isNull;

public class SystemManager {

    private static final Cache<Class<? extends EntitySystem>, Order.SystemOrder> CACHE = new Cache<>();
    private static final Comparator<EntitySystem> SYSTEM_COMPARATOR = Comparator.comparing(SystemManager::orderOf);
    private final List<EntitySystem> systems = new ArrayList<>();
    private final EntityManager entityManager;
    private final Engine engine;
    private boolean delayChanges = false;

    private final List<EntitySystem> pendingSystemsToAdd = new ArrayList<>();
    private final List<Class<? extends EntitySystem>> pendingSystemsToRemove = new ArrayList<>();

    public SystemManager(final Engine engine, final EntityManager entityManager) {
        this.engine = engine;
        this.entityManager = entityManager;
    }

    public void addSystem(final EntitySystem system) {
        if (delayChanges) {
            pendingSystemsToAdd.add(system);
        } else {
            systems.add(system);
            systems.sort(SYSTEM_COMPARATOR);
        }
    }

    public void addSystem(final EntitySystem system, final Order.SystemOrder order) {
        CACHE.put(system.getClass(), order);
        addSystem(system);
    }

    private static Order.SystemOrder orderOf(final EntitySystem entitySystem) {
        final var clazz = entitySystem.getClass();
        return CACHE.getOrElse(clazz, () -> {
            final var order = clazz.getAnnotation(Order.class);
            return isNull(order) ? Order.SystemOrder.SIMULATION : order.value();
        });
    }

    public List<EntitySystem> allSystems() {
        return systems;
    }

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

        for (final var systemType : pendingSystemsToRemove) {
            removeSystem(systemType);
        }

        pendingSystemsToRemove.clear();

        for (final var system : pendingSystemsToAdd) {
            addSystem(system);
        }
        pendingSystemsToAdd.clear();
    }

    public void removeSystem(final Class<? extends EntitySystem> systemType) {
        if (delayChanges) {
            pendingSystemsToRemove.add(systemType);
        } else {
            for (final var system : systemsOfType(systemType)) {
                systems.remove(system);
            }
        }
    }

    private List<EntitySystem> systemsOfType(final Class<? extends EntitySystem> type) {
        final List<EntitySystem> systemsOfType = new ArrayList<>();
        for (final var system : systems) {
            if (type == system.getClass()) {
                systemsOfType.add(system);
            }
        }
        return systemsOfType;
    }

    public boolean isSystemPresent(final Class<? extends EntitySystem> type) {
        for (final var system : systems) {
            if (type == system.getClass()) {
                return true;
            }
        }
        return false;
    }

}
