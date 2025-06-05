package dev.screwbox.core.environment.internal;

import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntityEvent;
import dev.screwbox.core.environment.EntityListener;

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
    private final List<Entity> pendingNewEntities = new ArrayList<>();
    private final List<Entity> pendingEntityDeletions = new ArrayList<>();
    private final List<Entity> pendingEntityCachesToRefresh = new ArrayList<>();
    private boolean delayChanges = false;

    public void addEntity(final Entity entity) {
        if (delayChanges) {
            pendingNewEntities.add(entity);
        } else {
            entity.id().ifPresent(id -> {
                if (nonNull(entitiesById.put(id, entity))) {
                    throw new IllegalStateException("duplicate entity id detected: " + id);
                }
            });
            entity.registerListener(this);
            refreshCachedArchetypes(entity);
            this.entities.add(entity);
        }
    }

    public List<Entity> allEntities() {
        return entities;
    }

    public List<Entity> entitiesMatching(final Archetype archetype) {
        Objects.requireNonNull(archetype, "archetype must not be null");
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
            final Archetype archetype = cacheSet.getKey();
            final var cacheEntities = cacheSet.getValue();
            if (archetype.matches(entity)) {
                if (!cacheEntities.contains(entity)) {
                    cacheEntities.add(entity);
                }
            } else {
                cacheEntities.remove(entity);
            }
        }
    }

    public void delayChanges() {
        this.delayChanges = true;
    }

    public void pickUpChanges() {
        this.delayChanges = false;

        if(pendingEntityCachesToRefresh.size() + pendingEntityDeletions.size() +  pendingNewEntities.size() > archetypeCache.size()) {
            archetypeCache.clear();
            pendingEntityCachesToRefresh.clear();
        }

        for (final Entity entity : pendingEntityDeletions) {
            removeEntity(entity);
        }
        pendingEntityDeletions.clear();

        for (final Entity entity : pendingNewEntities) {
            addEntity(entity);
        }
        pendingNewEntities.clear();

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
            entity.id().ifPresent(entitiesById::remove);
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
