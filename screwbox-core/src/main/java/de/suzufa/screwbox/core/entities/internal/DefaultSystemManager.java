package de.suzufa.screwbox.core.entities.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.Order;
import de.suzufa.screwbox.core.entities.UpdatePriority;

public class DefaultSystemManager implements SystemManager {

    private static final Comparator<EntitySystem> SYSTEM_COMPARATOR = (first, second) -> updatePriorityOf(first)
            .compareTo(updatePriorityOf(second));
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
            Collections.sort(systems, SYSTEM_COMPARATOR);
        }
    }

    private static UpdatePriority updatePriorityOf(EntitySystem entitySystem) {
        Time time = Time.now();
        // TODO: CACHE
        var order = entitySystem.getClass().getAnnotation(Order.class);
        UpdatePriority updatePriority = order == null ? UpdatePriority.SIMULATION : order.value();
        System.out.println(Duration.since(time).nanos());
        return updatePriority;
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
