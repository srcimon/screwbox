package io.github.simonbas.screwbox.core.entities;

import io.github.simonbas.screwbox.core.Engine;

import java.io.Serializable;

public interface EntityState extends Serializable {

    default void enter(final Entity entity, final Engine engine) {
    }

    EntityState update(final Entity entity, final Engine engine);

    default void exit(final Entity entity, final Engine engine) {
    }

}
