package dev.screwbox.core.navigation.internal;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;

import java.util.function.Predicate;

public class EntityContainsPositionFilter implements Predicate<Entity> {

    private final Vector position;

    public EntityContainsPositionFilter(final Vector position) {
        this.position = position;
    }

    @Override
    public boolean test(Entity entity) {
        return !entity.bounds().contains(position);
    }
}
