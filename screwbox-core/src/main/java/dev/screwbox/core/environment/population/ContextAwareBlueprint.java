package dev.screwbox.core.environment.population;

import dev.screwbox.core.environment.Entity;

@FunctionalInterface
public interface ContextAwareBlueprint<T>{

    Entity createFrom(T source, ImportContext context);
}
