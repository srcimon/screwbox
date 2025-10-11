package dev.screwbox.core.navigation.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Entity;

import java.util.function.Predicate;

public class EntityNotInRangeFilter implements Predicate<Entity> {

    private final Bounds range;

    public EntityNotInRangeFilter(final Bounds range) {
        this.range = range;
    }

    @Override
    public boolean test(final Entity entity) {
        return !range.intersects(entity.bounds());
    }

}
