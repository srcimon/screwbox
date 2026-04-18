package dev.screwbox.core.navigation;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.utils.MathUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Objects.isNull;

//TODO add to performance guide
//TODO document
//TODO test
public class SpacialIndex {

    private static final int NO_INDEX_NEEDED_COUNT = 50;//TODO find good value
    private final List<Entity> entities = new ArrayList<>();
    private SpacialHashRegistry registry;
    private int minCellSize = 2;

    public void refresh(final List<Entity> entities) {
        this.entities.clear();
        this.entities.addAll(entities);
        this.registry = null;
    }

    public List<Entity> findEntities(final Vector position, final double radius, final Predicate<Entity> entityFilter) {
        final Collection<Entity> prefetchEntities = prefetchEntities(position, radius);
        final List<Entity> nearbyEntities = new ArrayList<>();
        for (final var entity : prefetchEntities) {
            if (entity.position().distanceTo(position) <= radius && entityFilter.test(entity)) {
                nearbyEntities.add(entity);
            }
        }
        return nearbyEntities;
    }

    public List<Entity> findEntities(final Vector position, final double radius) {
        final List<Entity> prefetchEntities = prefetchEntities(position, radius);
        final List<Entity> nearbyEntities = new ArrayList<>();
        for (final var entity : prefetchEntities) {
            if (entity.position().distanceTo(position) <= radius) {
                nearbyEntities.add(entity);
            }
        }
        return nearbyEntities;
    }

    private List<Entity> prefetchEntities(final Vector position, final double radius) {
        if (entities.size() <= NO_INDEX_NEEDED_COUNT) {
            return entities;
        }

        if (isNull(registry) || registry.cellSize() < radius) {
            registry = new SpacialHashRegistry(calculateNextCellSize(radius), entities);
        }

        return registry.queryLocalBuckets(position);
    }

    private int calculateNextCellSize(final double radius) {
        final int cellSizeByRadius = MathUtil.nextHighestPowerOfTwoNumber(radius);
        minCellSize = Math.max(minCellSize, cellSizeByRadius);
        return minCellSize;
    }


}
