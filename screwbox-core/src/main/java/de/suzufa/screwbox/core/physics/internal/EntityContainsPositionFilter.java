package de.suzufa.screwbox.core.physics.internal;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.components.TransformComponent;

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
