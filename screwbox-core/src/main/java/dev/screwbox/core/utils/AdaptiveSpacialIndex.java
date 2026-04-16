package dev.screwbox.core.utils;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;

import java.util.ArrayList;
import java.util.List;

public class AdaptiveSpacialIndex {

    private static final int NO_INDEX_NEEDED_COUNT = 250;
    private final List<Entity> entities;

    public AdaptiveSpacialIndex(final List<Entity> entities) {
        this.entities = entities;
    }

    public List<Entity> findEntities(final Vector position, final double radius) {
        List<Entity> allEntities = prefetchEntities(radius);
        return filterEntitiesInDetail(position, radius, allEntities);
    }

    private List<Entity> prefetchEntities(final double radius) {
        if(entities.size() <= NO_INDEX_NEEDED_COUNT) {
            return entities;
        }

        //TODO find best suiting spacial index;
        //TODO already has index?
        var index = new SpacialIndex(radius, entities);
        return entities;//TODO fix
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
