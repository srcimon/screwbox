package de.suzufa.screwbox.core.physics.internal;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;

public class EntityNotInBounds implements EntitySearchFilter {

    private final Bounds bounds;

    public EntityNotInBounds(final Bounds bounds) {
        this.bounds = bounds;
    }

    @Override
    public boolean matches(final Entity entity) {
        return !bounds.intersects(entity.get(TransformComponent.class).bounds);
    }

}
