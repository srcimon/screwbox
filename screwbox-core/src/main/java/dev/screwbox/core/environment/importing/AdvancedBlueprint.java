package dev.screwbox.core.environment.importing;

import dev.screwbox.core.environment.Entity;

/**
 * An advanced {@link Blueprint} that can make use of the {@link ImportContext} to assemble an specific {@link Entity}
 * e.g. an enemy from a specific source.
 *
 * @see Blueprint
 * @see ComplexBlueprint
 * @since 3.19.0
 */
@FunctionalInterface
public interface AdvancedBlueprint<T> {

    /**
     * Assemble the {@link Entity} from the source.
     */
    Entity assembleFrom(T source, ImportContext context);

}
