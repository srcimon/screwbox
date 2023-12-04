package io.github.srcimon.screwbox.core.physics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.components.TransformComponent;

import java.util.function.Predicate;

public class EntityNotInRangeFilter implements Predicate<Entity> {

    private final Bounds range;

    public EntityNotInRangeFilter(final Bounds range) {
        this.range = range;
    }

    @Override
    public boolean test(final Entity entity) {
        return !range.intersects(entity.get(TransformComponent.class).bounds);
    }

}
