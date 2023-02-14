package de.suzufa.screwbox.core.physics.internal;

import de.suzufa.screwbox.core.entities.Component;
import de.suzufa.screwbox.core.entities.Entity;

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
