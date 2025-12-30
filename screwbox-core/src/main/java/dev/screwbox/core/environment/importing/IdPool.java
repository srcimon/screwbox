package dev.screwbox.core.environment.importing;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;

/**
 * Pool for
 *
 * @since 3.19.0
 */
public interface IdPool {

    /**
     * Allocates an artificial id that is not already present within the {@link Environment}.
     * Allocated ids are always negative. Allocating ids does not block this ids from being added manually to the {@link Environment}.
     *
     * @see Environment#allocateId()
     */
    int allocateId();

    /**
     * Peeks the next artificial id that will be allocated without actually allocating it.
     * Peeked ids are always negative.
     *
     * @see #allocateId()
     * @see Environment#peekId()
     */
    int peekId();

}
