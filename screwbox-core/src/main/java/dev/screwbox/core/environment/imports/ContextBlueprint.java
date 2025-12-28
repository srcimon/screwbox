package dev.screwbox.core.environment.imports;

import dev.screwbox.core.environment.Entity;

public interface ContextBlueprint<T> {

    Entity create(T source, ImportContext context);
}
