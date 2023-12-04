package io.github.srcimon.screwbox.core.environment;

import java.util.EventObject;

/**
 * Occurs when a {@link Component} is added or removed from an {@link Entity}.
 */
public class EntityEvent extends EventObject {

    public EntityEvent(final Entity source) {
        super(source);
    }

    /**
     * Returns the {@link Entity} where the {@link Component} was added or removed from.
     */
    public Entity entity() {
        return (Entity) source;
    }
}
