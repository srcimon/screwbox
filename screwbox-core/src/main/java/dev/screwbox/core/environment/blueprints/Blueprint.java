package dev.screwbox.core.environment.blueprints;

import dev.screwbox.core.environment.Entity;

public interface Blueprint<T> {

    Entity create(T source, ImportContext context);

}
