package dev.screwbox.core.environment.bulkimport;

import dev.screwbox.core.environment.Entity;

public interface Blueprint<T> {

    Entity create(T source);
}
