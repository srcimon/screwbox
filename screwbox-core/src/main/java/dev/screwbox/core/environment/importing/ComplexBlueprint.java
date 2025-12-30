package dev.screwbox.core.environment.importing;

import dev.screwbox.core.environment.Entity;

import java.util.List;

/**
 * A complex {@link Blueprint} that can make use of the {@link IdPool} to assemble multiple related {@link Entity entities}
 * e.g. a soft body enemy.
 *
 * @see AdvancedBlueprint
 * @see ComplexBlueprint
 * @since 3.19.0
 */
@FunctionalInterface
public interface ComplexBlueprint<T> {

    /**
     * Assemble the {@link Entity entities} from the source.
     */
    List<Entity> assembleFrom(T source, IdPool idPool);
}
