package dev.screwbox.core.physics.internal;

import dev.screwbox.core.environment.Entity;

import java.util.List;
import java.util.function.Predicate;

public final class EntityIsInRaycastFilter implements Predicate<Entity> {

    private final List<Entity> filteredEntities;

    public EntityIsInRaycastFilter(final Entity... filteredEntities) {
        this.filteredEntities = List.of(filteredEntities);
    }

    @Override
    public boolean test(final Entity entity) {
        return filteredEntities.contains(entity);
    }

}
