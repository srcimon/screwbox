package dev.screwbox.core.environment.logic;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Entity;

import java.io.Serializable;

@FunctionalInterface
public interface EntityState extends Serializable {

    default void enter(final Entity entity, final Engine engine) {
    }

    EntityState update(final Entity entity, final Engine engine);

    default void exit(final Entity entity, final Engine engine) {
    }
}
