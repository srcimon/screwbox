package dev.screwbox.core.environment.ingest;

import dev.screwbox.core.environment.Entity;

public interface Blueprint<T> {

    Entity create(T source);
}
