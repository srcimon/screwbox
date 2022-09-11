package de.suzufa.screwbox.core.physics.internal;

import de.suzufa.screwbox.core.entities.Component;
import de.suzufa.screwbox.core.entities.Entity;

public class EntityHasComponentFilter implements EntitySearchFilter {

    private final Class<? extends Component> componentClass;

    public EntityHasComponentFilter(final Class<? extends Component> componentClass) {
        this.componentClass = componentClass;
    }

    @Override
    public boolean matches(Entity entity) {
        return entity.hasComponent(componentClass);
    }

}
