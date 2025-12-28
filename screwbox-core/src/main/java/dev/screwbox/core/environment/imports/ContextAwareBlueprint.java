package dev.screwbox.core.environment.imports;

import dev.screwbox.core.environment.Entity;

public interface ContextAwareBlueprint<T> {

    Entity create(T source, ImportContext context);
}
