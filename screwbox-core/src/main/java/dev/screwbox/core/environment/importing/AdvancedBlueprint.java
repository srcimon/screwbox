package dev.screwbox.core.environment.importing;

import dev.screwbox.core.environment.Entity;

@FunctionalInterface
public interface AdvancedBlueprint<T> {

    Entity assembleFrom(T source, ImportContext context);

}
