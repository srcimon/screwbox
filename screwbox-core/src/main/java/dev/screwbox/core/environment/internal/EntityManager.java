package dev.screwbox.core.environment.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntityEvent;
import dev.screwbox.core.environment.EntityListener;
import dev.screwbox.core.utils.Reflections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.nonNull;

public class EntityManager implements EntityListener {

    private static final int CACHE_MIN_SIZE = 4;

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

        if (archetypeCache.size() > CACHE_MIN_SIZE && pendingEntityCachesToRefresh.size() + pendingEntityDeletions.size() + pendingNewEntities.size() > archetypeCache.size()) {
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

    public boolean idIsPresent(final int id) {
        return entitiesById.containsKey(id);
    }

    public void bake(Class<? extends Component> identifier, Class<? extends Component> bake) {
        boolean done = false;
        while (!done) {
            done = runBakeIteration(identifier, bake);
            pickUpChanges();
        }
    }

    private boolean runBakeIteration(final Class<? extends Component> identifier, final Class<? extends Component> bakeComponent) {
        final List<Entity> candidates = entitiesMatching(Archetype.ofSpacial(identifier, bakeComponent));
        final List<Entity> processedEntities = new ArrayList<>();
        final List<Entity> toAdd = new ArrayList<>();
        boolean done = true;

        for (int i = 0; i < candidates.size(); i++) {
            final var entity = candidates.get(i);

            if (!processedEntities.contains(entity)) {
                boolean bakedFound = false;
                for (int j = i + 1; j < candidates.size() && !bakedFound; j++) {
                    final var peer = candidates.get(j);

                    if (!processedEntities.contains(peer)) {
                        final var baked = tryBake(entity, peer, bakeComponent);

                        if (baked != null) {
                            var old = entity.get(identifier);
                            processedEntities.add(entity);
                            processedEntities.add(peer);

                            baked.add(old);
                            toAdd.add(baked);

                            done = false;
                            bakedFound = true;
                        }
                    }
                }
            }
        }

        for (final var entity : processedEntities) {
            if (entity.componentCount() == 2 && entity.hasTag("bake-result")) {
                removeEntity(entity);
            }
        }
        for (final var entity : toAdd) {
            addEntity(entity);
        }
        return done;
    }

    private static Entity tryBake(final Entity entity, final Entity peer, Class<? extends Component> componentClass) {
        final var entityComponent = entity.get(componentClass);
        final var peerComponent = peer.get(componentClass);
        final Optional<Bounds> result = entity.bounds().tryMerge(peer.bounds());
        if (result.isEmpty()) {
            return null;
        }

        if (Reflections.areEqualComparingFieldValues(entityComponent, peerComponent)) {
            final var bakeResult = new Entity()
                .bounds(result.get())
                .add(entity.get(componentClass))
                .tag("bake-result");

            entity.remove(componentClass);
            peer.remove(componentClass);
            return bakeResult;
        }
        return null;
    }
}
