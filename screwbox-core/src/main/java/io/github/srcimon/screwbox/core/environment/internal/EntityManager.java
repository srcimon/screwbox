package io.github.srcimon.screwbox.core.environment.internal;

import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntityEvent;
import io.github.srcimon.screwbox.core.environment.EntityListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.nonNull;

public class EntityManager implements EntityListener {

    private final List<Entity> entities = new ArrayList<>();
    private final Map<Integer, Entity> entitiesById = new HashMap<>();
    private final Map<Archetype, List<Entity>> archetypeCache = new HashMap<>();
    private final List<Entity> pendingNewEntites = new ArrayList<>();
    private final List<Entity> pendingEntityDeletions = new ArrayList<>();
    private final List<Entity> pendingEntityCachesToRefresh = new ArrayList<>();
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

    public List<Entity> entitiesMatching(final Archetype archetype) {
        Objects.requireNonNull(archetype, "archetype must not be null");//TODO add test
        final List<Entity> cacheResult = archetypeCache.get(archetype);
        if (nonNull(cacheResult)) {
            return cacheResult;
        }
        final List<Entity> calculatedResult = new ArrayList<>();
        for (final var entity : entities) {
            if (archetype.matches(entity)) {
                calculatedResult.add(entity);
            }
        }
        archetypeCache.put(archetype, calculatedResult);
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
    public void componentAdded(final EntityEvent event) {
        pendingEntityCachesToRefresh.add(event.entity());
    }

    public void removeEntity(final Entity entity) {
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
    public void componentRemoved(final EntityEvent event) {
        if (event.entity().isEmpty()) {
            pendingEntityDeletions.add(event.entity());
        } else {
            pendingEntityCachesToRefresh.add(event.entity());
        }
    }

    public Entity findById(final int id) {
        return entitiesById.get(id);
    }
}
