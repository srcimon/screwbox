package dev.screwbox.core.environment.importing;

import dev.screwbox.core.environment.Entity;

/**
 * Blueprint that can assemble an specific {@link Entity} e.g. an enemy from a specific source.
 *
 * @see AdvancedBlueprint
 * @see ComplexBlueprint
 * @since 3.19.0
 */
@FunctionalInterface
public interface Blueprint<T> {

    /**
     * Assemble the {@link Entity} from the source.
     */
    Entity assembleFrom(T source);

}
