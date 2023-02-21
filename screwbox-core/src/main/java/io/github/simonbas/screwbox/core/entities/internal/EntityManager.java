package io.github.simonbas.screwbox.core.entities.internal;

import io.github.simonbas.screwbox.core.entities.Archetype;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.EntityListener;

import java.util.*;

import static java.util.Objects.nonNull;

public class EntityManager implements EntityListener {

    private final List<Entity> entities = new ArrayList<>();
    private final Map<Integer, Entity> entitiesById = new HashMap<>();
    private final Map<Archetype, List<Entity>> archetypeCache = new HashMap<>();
    private final Set<Entity> pendingNewEntites = new HashSet<>();
    private final Set<Entity> pendingEntityDeletions = new HashSet<>();
    private final Set<Entity> pendingEntityCachesToRefresh = new HashSet<>();
    private boolean delayChanges = false;

    public void addEntity(final Entity entity) {
        if (delayChanges) {
            pendingNewEntites.add(entity);
        } else {
            final var id = entity.id();
            if (id.isPresent()) {
                if (entitiesById.containsKey(id.get())) {
                    throw new IllegalStateException("duplicate entity id detected: " + id.get());
                }
                entitiesById.put(id.get(), entity);
            }
            entity.registerListener(this);
            refreshCachedArchetypes(entity);
            this.entities.add(entity);
        }
    }

    public List<Entity> allEntities() {
        return entities;
    }

    public List<Entity> entitiesMatching(final Archetype componentGroup) {
        final List<Entity> cacheResult = archetypeCache.get(componentGroup);
        if (nonNull(cacheResult)) {
            return cacheResult;
        }
        final List<Entity> calculatedResult = new ArrayList<>();
        for (final var entity : entities) {
            if (componentGroup.matches(entity)) {
                calculatedResult.add(entity);
            }
        }
        archetypeCache.put(componentGroup, calculatedResult);
        return calculatedResult;
    }

    private void refreshCachedArchetypes(final Entity entity) {
        for (final var cacheSet : archetypeCache.entrySet()) {
            final Archetype arechetype = cacheSet.getKey();
            if (arechetype.matches(entity)) {
                final var cacheEntities = cacheSet.getValue();
                if (!cacheEntities.contains(entity)) {
                    cacheEntities.add(entity);
                }
            } else {
                archetypeCache.get(arechetype).remove(entity);
            }
        }
    }

    public void delayChanges() {
        this.delayChanges = true;
    }

    public void pickUpChanges() {
        this.delayChanges = false;

        for (final Entity entity : pendingEntityDeletions) {
            removeEntity(entity);
        }
        pendingEntityDeletions.clear();

        for (final Entity entity : pendingNewEntites) {
            addEntity(entity);
        }
        pendingNewEntites.clear();

        for (final Entity entity : pendingEntityCachesToRefresh) {
            refreshCachedArchetypes(entity);
        }
        pendingEntityCachesToRefresh.clear();
    }

    @Override
    public void componentAdded(final Entity entity) {
        pendingEntityCachesToRefresh.add(entity);
    }

    public void removeEntity(final Entity entity) {
        pendingEntityCachesToRefresh.remove(entity);

        if (delayChanges) {
            pendingEntityDeletions.add(entity);
        } else {
            for (final var cacheEntry : archetypeCache.values()) {
                cacheEntry.remove(entity);
            }
            this.entities.remove(entity);
            final var id = entity.id();
            id.ifPresent(entitiesById::remove);
        }
    }

    @Override
    public void componentRemoved(final Entity entity) {
        if (entity.isEmpty()) {
            pendingEntityDeletions.add(entity);
        } else {
            pendingEntityCachesToRefresh.add(entity);
        }
    }

    public Entity findById(final int id) {
        return entitiesById.get(id);
    }

}
