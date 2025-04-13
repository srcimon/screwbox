package dev.screwbox.core.physics.internal;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.util.function.Predicate;

public class EntityHasComponentFilter implements Predicate<Entity> {

    private final Class<? extends Component> componentClass;

    public EntityHasComponentFilter(final Class<? extends Component> componentClass) {
        this.componentClass = componentClass;
    }

    @Override
    public boolean test(Entity entity) {
        return entity.hasComponent(componentClass);
    }

}
