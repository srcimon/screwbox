package dev.screwbox.core.environment.setup;

import dev.screwbox.core.environment.Entity;

@FunctionalInterface
public interface Blueprint<T>{

    Entity createFrom(T source);
}
