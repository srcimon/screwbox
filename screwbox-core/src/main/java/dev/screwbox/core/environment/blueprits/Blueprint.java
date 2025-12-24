package dev.screwbox.core.environment.blueprits;

import dev.screwbox.core.environment.Entity;

public interface Blueprint<T> {

    Entity create(T source);
}
