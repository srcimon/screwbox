package dev.screwbox.core.environment.bulkimport;

import dev.screwbox.core.environment.Entity;

public interface ContextAwareBlueprint<T> {

    Entity create(T source, ImportContext context);
}
