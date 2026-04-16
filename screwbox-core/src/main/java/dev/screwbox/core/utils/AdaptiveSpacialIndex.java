package dev.screwbox.core.utils;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

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

        //TODO find best suiting spacial index;
        //TODO already has index?
        if (nonNull(index)) {
            if (index.cellSize() > radius) {
                index = new SpacialIndex( nextHighestPowerOfTwoNumber(radius), entities);
            }
            return index.findEntities(position, radius);
        }
        index = new SpacialIndex(radius, entities);
        return index.findEntities(position, radius);
    }

    //TODO duplicate MathUtil?
    private static int nextHighestPowerOfTwoNumber(final double value) {
            if (value <= 0) return 1;
            // Math.log2 existiert in Java nicht direkt, daher log(x)/log(2)
            double exponent = Math.ceil(Math.log(value) / Math.log(2));
            return (int)Math.pow(2, exponent);
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
