package de.suzufa.screwbox.core.physics.internal;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;

public class EntityNotInRangeFilter implements EntitySearchFilter {

    private final Bounds range;

    public EntityNotInRangeFilter(final Bounds range) {
        this.range = range;
    }

    @Override
    public boolean matches(final Entity entity) {
        return !range.intersects(entity.get(TransformComponent.class).bounds);
    }

}
