package de.suzufa.screwbox.core.physics.internal;

import java.util.List;

import de.suzufa.screwbox.core.entityengine.Entity;

public final class EntityIsInRaycastFilter implements EntitySearchFilter {

    private final List<Entity> filteredEntities;

    public EntityIsInRaycastFilter(final Entity... filteredEntities) {
        this.filteredEntities = List.of(filteredEntities);
    }

    @Override
    public boolean matches(final Entity entity) {
        return filteredEntities.contains(entity);
    }

}
