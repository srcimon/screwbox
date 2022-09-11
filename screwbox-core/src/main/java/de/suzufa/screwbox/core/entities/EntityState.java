package de.suzufa.screwbox.core.entities;

import java.io.Serializable;

import de.suzufa.screwbox.core.Engine;

public interface EntityState extends Serializable {

    default void enter(final Entity entity, final Engine engine) {
    }

    EntityState update(final Entity entity, final Engine engine);

    default void exit(final Entity entity, final Engine engine) {
    }

}
