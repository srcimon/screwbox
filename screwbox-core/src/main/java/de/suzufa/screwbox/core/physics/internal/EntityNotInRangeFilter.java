package de.suzufa.screwbox.core.physics.internal;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.TransformComponent;

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
