package dev.screwbox.core.navigation;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.utils.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Objects.isNull;

public class AdaptiveSpacialIndex {

    private static final int NO_INDEX_NEEDED_COUNT = 50;//TODO find good value
    private final List<Entity> entities;
    private SpacialIndex index;

    public AdaptiveSpacialIndex(final List<Entity> entities) {
        this.entities = entities;
    }

    public List<Entity> findEntities(final Vector position, final double radius, final Predicate<Entity> entityFilter) {
        final List<Entity> prefetchEntities = prefetchEntities(position, radius);
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

        if (isNull(index) || index.cellSize() < radius) {
            index = new SpacialIndex(MathUtil.nextHighestPowerOfTwoNumber(radius), entities);
        }

        return index.queryLocalBuckets(position);
    }

    public Optional<Double> cellSize() {
        return Optional.ofNullable(index).map(SpacialIndex::cellSize);
    }

}
