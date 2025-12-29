package dev.screwbox.core.environment.importing;

import dev.screwbox.core.environment.Entity;

import java.util.List;

@FunctionalInterface
public interface ComplexBlueprint<T> {

    List<Entity> assembleFrom(T source, ImportContext context);
}
