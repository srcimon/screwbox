package io.github.srcimon.screwbox.core.physics.internal;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.components.TransformComponent;

import java.util.function.Predicate;

public class EntityContainsPositionFilter implements Predicate<Entity> {

    private final Vector position;

    public EntityContainsPositionFilter(final Vector position) {
        this.position = position;
    }

    @Override
    public boolean test(Entity entity) {
        return !entity.get(TransformComponent.class).bounds.contains(position);
    }
}
