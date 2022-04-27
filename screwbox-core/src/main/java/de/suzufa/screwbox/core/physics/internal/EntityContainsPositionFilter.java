package de.suzufa.screwbox.core.physics.internal;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;

public class EntityContainsPositionFilter implements EntitySearchFilter {

    private final Vector position;

    public EntityContainsPositionFilter(final Vector position) {
        this.position = position;
    }

    @Override
    public boolean matches(final Entity entity) {
        return !entity.get(TransformComponent.class).bounds.contains(position);
    }

}
