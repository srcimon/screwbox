package dev.screwbox.core.utils;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

public class AdaptiveSpacialIndex {

    private static final int NO_INDEX_NEEDED_COUNT = 250;
    private final List<Entity> entities;
    private SpacialIndex index;

    public AdaptiveSpacialIndex(final List<Entity> entities) {
        this.entities = entities;
    }

    public List<Entity> findEntities(final Vector position, final double radius) {
        List<Entity> allEntities = prefetchEntities(position, radius);
        return filterEntitiesInDetail(position, radius, allEntities);
    }

    private List<Entity> prefetchEntities(final Vector position, final double radius) {
        if (entities.size() <= NO_INDEX_NEEDED_COUNT) {
            return entities;
        }

        if (isNull(index) || index.cellSize() < radius) {
            index = new SpacialIndex(MathUtil.nextHighestPowerOfTwoNumber(radius), entities);
        }

        return index.findEntities(position, radius);
    }

    public Optional<Double> cellSize() {
        return Optional.ofNullable(index).map(SpacialIndex::cellSize);
    }

    private static List<Entity> filterEntitiesInDetail(final Vector position, final double radius, final List<Entity> allEntities) {
        final List<Entity> nearbyEntities = new ArrayList<>();
        for (var entity : allEntities) {
            if (entity.position().distanceTo(position) <= radius) {
                nearbyEntities.add(entity);
            }
        }
        return nearbyEntities;
    }
}
