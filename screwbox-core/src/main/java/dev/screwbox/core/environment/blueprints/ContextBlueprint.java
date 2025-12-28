package dev.screwbox.core.environment.blueprints;

import dev.screwbox.core.environment.Entity;

public interface ContextBlueprint<T> {

    Entity create(T source, ImportContext context);
}
